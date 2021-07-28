
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

		let url=`http://localhost:8080/admin/search-book/${query}`;
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

		let url=`http://localhost:8080/admin/search/${query}`;
		fetch(url)
		.then((response)=>{
			return response.json();
		})
		.then((data)=>{
			let text=`<div class="list-group">`
			
			data.forEach((book)=>{
				text+=`<a href='/admin/show-book/${book.book_id}' class='list-group-item list-group-action'> ${book.book_name}</a>`
			});
			
			
			text+=`</div>`
			
			$(".search-result").html(text);
			$(".search-result").show();
		});
		
		
	}
	
};

