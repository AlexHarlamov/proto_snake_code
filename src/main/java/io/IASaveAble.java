package io;

import io.network.sender.MessageSender;

public interface IASaveAble {
    void saveIAPair(MessageSender.AddressPair hostInfo);
    MessageSender.AddressPair getHostAI();
}
