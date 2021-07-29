
console.log("working fine");

const toggleSidebar=()=>{
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display" , "none");
		$(".sidebar").css("visibility" , "hidden");
		$(".content").css("margin-left" , "0%");
		$(".toggle").css("visibility", "visible");
		console.log("close");
	}
	else{
		$(".sidebar").css("display" , "block");
		$(".sidebar").css("visibility" , "visible");
		$(".content").css("margin-left" , "18%");
		$(".toggle").css("visibility", "hidden");
		console.log("open");
	}
}

const search=()=>{
	
	let query=$("#search_book").val();
	
	if(query==''){
		
	}
	else{

		let url=`http://localhost:8080/user/search-book/${query}`;
		window.location=url;
		console.log(query);
	}
	
}

const searchonkeyup=()=>{
	
	let query=$("#search_book").val();
	
	if(query==''){
		$(".search-result").hide();
	}
	else{

		let url=`http://localhost:8080/user/search/${query}`;
		fetch(url)
		.then((response)=>{
			return response.json();
		})
		.then((data)=>{
			let text=`<div class="list-group">`
			
			data.forEach((book)=>{
				text+=`<a href='/user/show-book/${book.book_id}' class='list-group-item list-group-action'> ${book.book_name}</a>`
			});
			
			
			text+=`</div>`
			
			$(".search-result").html(text);
			$(".search-result").show();
		});
		
		
	}
	
};



const releaseto=(book_id)=>{
	
	let txt=`.release-button`;
	txt+=`${book_id}`;
	let releaseto=`.release-to-user`;
	releaseto+=`${book_id}`;
	
	
	if($(txt).is(":visible")){
		$(txt).css("display" , "none");
		$(txt).css("visibility" , "hidden");
		$(releaseto).css("display" , "block");
		$(releaseto).css("visibility" , "visible");
		console.log(txt);
	}
	else{
		$(releaseto).css("display" , "none");
		$(releaseto).css("visibility" , "hidden");
		$(txt).css("display" , "block");
		$(txt).css("visibility" , "visible");
	}
}


const reviewto=(book_id)=>{
	
	let txt=`.review-button`;
	txt+=`${book_id}`;
	let reviewto=`.review-field`;
	reviewto+=`${book_id}`;
	
	
	if($(txt).is(":visible")){
		$(txt).css("display" , "none");
		$(txt).css("visibility" , "hidden");
		$(reviewto).css("display" , "block");
		$(reviewto).css("visibility" , "visible");
		console.log(txt);
	}
	else{
		$(reviewto).css("display" , "none");
		$(reviewto).css("visibility" , "hidden");
		$(txt).css("display" , "block");
		$(txt).css("visibility" , "visible");
	}
}


const refreshusers=(receiverid)=>{

		let url=`http://localhost:8080/user/chat/refresh/users`;
		fetch(url)
		.then((response)=>{
			return response.json();
		})
		.then((data)=>{
			let text=`<div class='messageusers'>`
			data.forEach((sendersAndReceivers)=>{

				
				
				/*<div class="senderAndReceiver" th:classappend="${sr.user_id==receiver.user_id?'activechat':'otherchat'}">
	<img class="profile-image chat-avatar" th:src="@{'/images/'+${sr.image}}" alt="avatar"/>
	<a  th:href="@{'/user/chat/'+${sr.user_id}}" class="chat" id="click-to-chat" ><span th:text="${sr.user_name}"></span></a>
	</div>
	
		<div th:each="sr: ${sendersAndReceiversList}" >
	<div class="senderAndReceiver" th:classappend="${sr.user_id==receiver.user_id?'activechat':'otherchat'}">
	<img class="profile-image chat-avatar" th:src="@{'/images/'+${sr.image}}" alt="avatar"/>
	<a  th:href="@{'/user/chat/'+${sr.user_id}}" class="chat" id="click-to-chat" ><span th:text="${sr.user_name}"></span></a>
	</div>
	</div>*/
	
	
				text+=`<div class='${sendersAndReceivers.user_id==receiverid?'activechat':'otherchat'}'>`
				text+=`<img class='profile-image chat-avatar' src='/images/${sendersAndReceivers.image}' alt='avatar'/>`
				text+=`<a href='/user/chat/${sendersAndReceivers.user_id}' class='chat' id='click-to-chat'>${sendersAndReceivers.user_name}</a><span class='dot notificationfrom${sendersAndReceivers.user_id}' style='visibility: hidden;'></span>`
				text+=`</div>`

			});
			
			text+=`</div>`
			console.log(text);

			$(".messageusers").html(text);
			$(".messageusers").show();
		});
	
}




function shownotification(){
	let url=`http://localhost:8080/user/chat/notification`;
	fetch(url)
	.then((response)=>{
		return response.json();
	})
	.then((data)=>{
		
		data.forEach((chatNotification)=>{
			
			if(chatNotification.seen_messages!=chatNotification.total_messages){
				let text=`.notificationfrom`;
				text+=`${chatNotification.sender_id}`;
				$(text).css("visibility" , "visible");
				$(".newmessage").css("visibility", "visible");

			}
			
		});
		
	});
}





var usercount=0
var prevusercount=0;
function getcount(){
	let url=`http://localhost:8080/user/chat/refresh/users/count`;
	fetch(url)
		.then((response)=>{
			return response.json();
		})
		.then((data)=>{
			
			usercount=data;
			if(usercount!=prevusercount){
				prevusercount=usercount;
				refreshusers(receiverid);
			}

		});
	
}


var count=0;
var max=0;
const refreshchat=(to_user, count)=>{
	shownotification();
	getcount();
	var val=0;
count++;
if(count<3){
	scroll();
}
		let url=`http://localhost:8080/user/chat/refresh/${to_user}`;
		fetch(url)
		.then((response)=>{
			return response.json();
		})
		.then((data)=>{
			let text=`<div class="">`
			data.forEach((chatMessage)=>{
				val++;
				if(chatMessage.sender_id==to_user){
					text+=`<a class='chat-messages-display-single-receiver'> ${chatMessage.message}</a>`
				}
				else{
					
					text+=`<a class='chat-messages-display-single-sender'> ${chatMessage.message}</a>`
				}

			});
			
			text+=`</div>`
			
			
			
			$(".chats").html(text);
			$(".chats").show();
			
		if(val!=max){
				scroll();
				max=val;
			}
		});
		
		
		
		$("#wrapperchat").load();
  setTimeout(function() {
    refreshchat(to_user, count);
  }, 400);
		

	
};
$(document).ready(function () {
  refreshchat(to_user, count);
});




var submitMessage=document.getElementById("submitMessage");
var clicktochat=document.getElementById("click-to-chat");
var receiver=document.getElementById("receiver");
var receiverid=receiver.getAttribute("value");


function save(receiverid, message){
	let url=`http://localhost:8080/user/chat/add/${receiverid}/${message}`;
	fetch(url)
		.then((response)=>{
			return response.json();
		})
		.then((data)=>{


		});
	
}


submitMessage.addEventListener('click', function(){
	let message=$("#chatmessage").val();
	console.log(receiverid);
	console.log(message);
	save(receiverid, message);
	refreshchat(receiverid, 0);
	document.getElementById('chatmessage').value = "";
	
	
});


window.addEventListener('load', function() {
    refreshchat(receiverid, 0);
    refreshusers(receiverid);
});

function enablechatbutton(){
	var input=document.getElementById("chatmessage");
	if(input==''){
		 document.getElementById('submitMessage').disabled = true;
	}
	else{
		document.getElementById('submitMessage').disabled = false;
	}
}

function scroll(){
	var chatHistory = document.getElementById("chatbody");
		chatHistory.scrollTop = chatHistory.scrollHeight;
}






/*---------------------------------NOTIFICATION-------------------------------------------*/












