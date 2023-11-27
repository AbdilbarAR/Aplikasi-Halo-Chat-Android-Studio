package com.littleit.halo_chat_2.interfaces;

import com.littleit.halo_chat_2.Model.chat.Chats;

import java.util.List;

public interface OnReadChatCallBack {
    void onReadSuccess(List<Chats> list);
    void onReadFailed();
}
