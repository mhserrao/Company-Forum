function getAlert() {
	alert("You are about to be amazed");
}

function getCount(id) {
	var this_message = "";
	if(id>1){
		this_message = "This thread was created with the " + id + " message in our database!";
	} else {
		this_message = "This thread was created with the first message on this website ever!";
	}
	alert(this_message);
}

