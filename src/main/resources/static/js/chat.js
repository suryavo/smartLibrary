const url = 'http://localhost:8080';
let stompClient;
let selectedUser;
let newMessages = new Map();
let userid=$("#useridvalue").val();
var sender=0;

var submitMessage=document.getElementById("submitMessage");
var clicktochat=document.getElementById("click-to-chat");
var receiver=document.getElementById("receiver");
var receiverid=0;
if(receiver!=null) receiverid=receiver.getAttribute("value");

window.addEventListener('load', function() {
    connectToChat(userid);
    refreshusers(receiverid);
});


function connectToChat(user_id) {
    console.log("connecting to chat...")
    let socket = new SockJS(url + '/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/topic/messages/" + user_id, function (response) {
            let data = JSON.parse(response.body);
            func(receiverid, 0);
            sender=data.sender_id;
            if (receiverid == data.sender_id) {
                render(data.message, data.sender_id);
            } else {
                let text=`.notificationfrom`;
				text+=`${data.sender_id}`;
				$(text).css("visibility" , "visible");
				$(".newmessage").css("visibility", "visible");
            }

        });
    });
}



function sendMsg(to, text) {
    stompClient.send("/app/chat/" + receiverid, {}, JSON.stringify({
        receiver_id: to,
        sender_id: userid,
        message: text
    }));
    func(receiverid, 0);

}


function sendMessage(message) {
    sendMsg(receiverid, message);
    
    let text=`<div class="">`
	text+=`<a class='chat-messages-display-single-sender'> ${message}</a>`
	text+=`</div>`
	console.log(text);
	document.getElementById("chatbody").innerHTML+=text;
	var v=document.getElementById("chatbody").innerHTML;

    console.log(message);
    setTimeout(function () {
        scroll();
    }.bind(this), 100);
}


if(submitMessage!=null){
	submitMessage.addEventListener('click', function(){
	let message=$("#chatmessage").val();
	console.log(message);
	sendMessage(message);
	save(receiverid, message);
	document.getElementById('chatmessage').value = "";

});
}


function render(message, sender) {
	scroll();
	console.log(message);
	
	
	let text=`<div class="">`
	text+=`<a class='chat-messages-display-single-receiver'> ${message}</a>`
	text+=`</div>`
	console.log(text);
	
	document.getElementById("chatbody").innerHTML+=text;

    setTimeout(function () {
      scroll();
    }.bind(this), 100);
}

/*.................................................................................................................................................*/

