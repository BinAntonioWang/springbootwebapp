package guru.springframework.services.optionCards;

import com.lark.oapi.card.model.CardAction;

public interface IOptionCardHandler {
    void handle(CardAction cardAction);
    Object getResponse(CardAction cardAction);
}
