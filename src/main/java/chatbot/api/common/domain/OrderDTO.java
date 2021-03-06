package chatbot.api.common.domain;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {
	private String msg;
	private HttpStatus status;
	private Object dataset;
	private LocalDateTime orderDateTime;
}