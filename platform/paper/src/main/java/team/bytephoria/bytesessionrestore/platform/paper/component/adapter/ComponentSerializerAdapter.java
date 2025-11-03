package team.bytephoria.bytesessionrestore.platform.paper.component.adapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public interface ComponentSerializerAdapter {

    ComponentSerializer<Component, ? extends Component, String> getSerializer();

}