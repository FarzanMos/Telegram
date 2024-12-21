package org.telegram.ui.Stories.recorder;

import static org.telegram.messenger.LocaleController.getString;

import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.ui.ChatActivity;

import org.telegram.tgnet.TLRPC;

public class ChatAttachPickerCameraRecorderInfo {

    public final CharSequence title;
    public final Delegate delegate;

    public ChatAttachPickerCameraRecorderInfo(ChatActivity chatActivity, Delegate delegate) {
        this.title = fetchTitle(chatActivity);
        this.delegate = delegate;
    }

    private CharSequence fetchTitle(ChatActivity chatActivity) {
        if (chatActivity != null) {
            TLRPC.Chat chat = null;
            TLRPC.User user = null;
            if (chatActivity.getChatMode() == ChatActivity.MODE_SAVED) {
                long did = chatActivity.getSavedDialogId();
                if (did >= 0) {
                    user = chatActivity.getMessagesController().getUser(did);
                } else {
                    chat = chatActivity.getMessagesController().getChat(-did);
                }
            } else {
                user = chatActivity.getCurrentUser();
                chat = chatActivity.getCurrentChat();
            }
            if (chat != null) {
                return chat.title;
            } else {
                if (UserObject.isUserSelf(user)) {
                    return getString(chatActivity.getChatMode() == ChatActivity.MODE_SAVED ? R.string.MyNotes : R.string.SavedMessages);
                } else if (UserObject.isAnonymous(user)) {
                    return getString(R.string.AnonymousForward);
                } else {
                    return UserObject.getUserName(user);
                }
            }
        }
        return "";
    }

    public interface Delegate {

        void onSend(StoryEntry outputEntry, String path);

        void onClose();
    }
}
