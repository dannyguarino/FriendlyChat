# FriendlyChat
An `End-to-End Encryption` based Online Chatting Android App

## Google Authentication
<p align="center">
  <img src="https://github.com/sbInfin1/FriendlyChat/blob/testingOutRoom/screenshots/samsungM30s/Screenshot_20210727-170719_Google%20Play%20services.jpg" width="270" height="585" title="google_auth_screen"/>
  <img src="https://github.com/sbInfin1/FriendlyChat/blob/testingOutRoom/screenshots/samsungM30s/Screenshot_20210727-170905_Friendly%20Chat.jpg" width="270" height="585" title="signed_in_toast"/>
</p>

The user can sign in to the `Friendly Chat` App using their Google account. `Friendly Chat` uses the `google` account as the primary key to refer to any friend that an user might want to chat with.

## Friends List
<p align="center">
  <img src="https://github.com/sbInfin1/FriendlyChat/blob/testingOutRoom/screenshots/samsungM30s/Screenshot_20210727-170934_Friendly%20Chat.jpg" width="270" height="585" title="friend_list_screen"/>
</p>

The `FriendListActivity` displays the list of all the friends that the user has added. More friends can be added by clicking on the `Add friend` floating action button. This opens up the `AddFriendActivity` where the user can enter the gmail address of the friend and the name field is optional. The new friend will be added only if the friend is currently using the `Friendly Chat` app.

## Message Room
<p align="center">
  <img src="https://github.com/sbInfin1/FriendlyChat/blob/testingOutRoom/screenshots/samsungM30s/Screenshot_20210727-171022_Friendly%20Chat.jpg" width="270" height="585" title="message_room_rishita_1"/>
  <img src="https://github.com/sbInfin1/FriendlyChat/blob/testingOutRoom/screenshots/samsungM11/Screenshot_20210727-171037_Friendly%20Chat.jpg" width="270" height="585" title="message_room_apple_pie_1"/>
</p>

This is a screenshot of the message room in the devices of `Rishita Burman` and `Apple pie`. The name of the person with whom the user is chatting will be shown on the Action bar of the corresponding message room. 

<p align="center">
  <img src="https://github.com/sbInfin1/FriendlyChat/blob/testingOutRoom/screenshots/samsungM30s/Screenshot_20210727-171057_Friendly%20Chat.jpg" width="270" height="585" title="message_room_rishita_2"/>
  <img src="https://github.com/sbInfin1/FriendlyChat/blob/testingOutRoom/screenshots/samsungM11/Screenshot_20210727-171054_Friendly%20Chat.jpg" width="270" height="585" title="essage_room_apple_pie_2"/>
</p>

Messages created by the user appears the right aligned and in pink `chat bubble` and those received from the friend will appear left aligned and in turquoise bubble.  Further, the timestamp is attached below every message.
