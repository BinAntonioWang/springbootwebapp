package guru.springframework.services.optionCards;

import com.lark.oapi.card.model.CardAction;
import org.springframework.stereotype.Service;

@Service
public class ApplyOptionCardHandler implements IOptionCardHandler{
    @Override
    public void handle(CardAction cardAction) {
        System.out.println("ApplyOptionCardHandler.handle");
    }

    @Override
    public Object getResponse(CardAction cardAction) {
        System.out.println("ApplyOptionCardHandler.getResponse");
        return null;
    }
}
