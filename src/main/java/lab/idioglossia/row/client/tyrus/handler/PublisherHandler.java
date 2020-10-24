package lab.idioglossia.row.client.tyrus.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lab.idioglossia.row.client.callback.SubscriptionListener;
import lab.idioglossia.row.client.exception.MessageDataProcessingException;
import lab.idioglossia.row.client.model.protocol.Naming;
import lab.idioglossia.row.client.model.protocol.ResponseDto;
import lab.idioglossia.row.client.pipeline.StoppablePipeline;
import lab.idioglossia.row.client.registry.SubscriptionListenerRegistry;
import lab.idioglossia.row.client.util.DefaultJacksonMessageConverter;

public class PublisherHandler implements StoppablePipeline.Stage<MessageHandlerInput, Void> {
    private final SubscriptionListenerRegistry subscriptionListenerRegistry;
    private final DefaultJacksonMessageConverter defaultJacksonMessageConverter;

    public PublisherHandler(SubscriptionListenerRegistry subscriptionListenerRegistry, DefaultJacksonMessageConverter defaultJacksonMessageConverter) {
        this.subscriptionListenerRegistry = subscriptionListenerRegistry;
        this.defaultJacksonMessageConverter = defaultJacksonMessageConverter;
    }

    @Override
    public boolean process(MessageHandlerInput input, Void aVoid) throws MessageDataProcessingException {
        ResponseDto responseDto = input.getResponseDto();
        if(responseDto.getHeaders().containsKey(Naming.SUBSCRIPTION_EVENT_HEADER_NAME) && !responseDto.getHeaders().containsKey(Naming.SUBSCRIPTION_Id_HEADER_NAME)){
            SubscriptionListenerRegistry.SubscriptionRegistryModel<?> subscriptionRegistryModel = subscriptionListenerRegistry.getSubscriptionListener(responseDto.getHeaders().get(Naming.SUBSCRIPTION_EVENT_HEADER_NAME));
            SubscriptionListener subscriptionListener = subscriptionRegistryModel.getSubscriptionListener();
            try {
                subscriptionListener.onMessage(subscriptionRegistryModel.getSubscription(), defaultJacksonMessageConverter.readJsonNode(responseDto.getBody(), subscriptionListener.getListenerMessageBodyClassType()));
            } catch (JsonProcessingException e) {
                throw new MessageDataProcessingException(e);
            }
            return false;
        }
        return true;
    }

}
