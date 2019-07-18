package chatbot.api.common.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // exchange, 라우터 설정
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(RabbitMQConstants.SKILL_EXCHANGE);
    }


    // extablish
    // 큐 생성
    @Bean
    Queue queueForEstablish() {
        return new Queue(RabbitMQConstants.SKILL_ESTABLISH_QUEUE, false);
    }

    // exchange (라우터) 와 Establish Queue 를 바인딩 시키고, exchange 에게 Establish Queue 로 접근하기 위한 라우팅 키를 알려줌
    @Bean
    Binding bindingForEstablish(@Qualifier("queueForEstablish") Queue queueEstablish, TopicExchange exchange) {
        return BindingBuilder.bind(queueEstablish).to(exchange).with(RabbitMQConstants.SKILL_ESTABLISH_ROUTE_KEY);
    }


    // keep-alive
    // 큐 생성
    @Bean
    Queue queueForKeepAlive() {
        return new Queue(RabbitMQConstants.SKILL_KEEP_ALIVE_QUEUE, false);
    }

    // exchange (라우터) 와 Keep-Alive Queue 를 바인딩 시키고, exchange 에게 Keep-Alive Queue 로 접근하기 위한 라우팅 키를 알려줌
    @Bean
    Binding bindingForKeepAlive(@Qualifier("queueForKeepAlive") Queue queueKeepAlive, TopicExchange exchange) {
        return BindingBuilder.bind(queueKeepAlive).to(exchange).with(RabbitMQConstants.SKILL_KEEP_ALIVE_ROUTE_KEY);
    }


    // 메시지 컨버터
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    // template
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }
}
