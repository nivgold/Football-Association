let username_global = "";
let roles_global = "";
var stompClient = null;

function showActions() {
	clearAllDisplay();
	document.getElementById("actions_title").style = "display: inline-block"
	document.getElementById("actions").style = "display: inline-block"
	// TODO replace this code
	// this function should call to the right function decided by the roles of the current user
	roles_global.forEach(role => {
			if (role.roleName == "TeamOwner") {
				owner_menu();
			}
			if (role.roleName == "Referee") {
				referee_menu();
			}
			if (role.roleName == "AssociationAgent") {
				agent_menu();
			}
	}); 
}

async function submitLogin() {
	//send request
	let username = document.getElementById("logName").value;
	let password = document.getElementById("logPswd").value;
	let loginReqResults="";
	
	try{
		loginReqResults = await loginRequest(username,password,"","");

		//check result
		if (loginReqResults==null) {
			alert("server error");
		}
		else {
			let status = loginReqResults.status;
			if (!(status=="true")) {
				alert("credintials are incorrect");
			}
			else {
				username_global = username;
				roles_global = loginReqResults.roles;
				subscribed_games = loginReqResults.subscribedGames;
				if (subscribed_games.length != 0){
					// connecting to the notification socket
					var socket = new SockJS('http://lvh.me:5001/gs-guide-websocket');
					stompClient = Stomp.over(socket);
					stompClient.connect({}, function (frame) {
						subscribed_games.forEach(currentGame => {
							stompClient.subscribe('/game/'+currentGame, function (notification_message) {
								alert(JSON.parse(notification_message.body).content);
							});
						});
					});
				}
				alert ("Hello " + username + "!");
				showActions(); 
			}
		}
	} catch(e){
		alert("cannot connect");
	}
	
}

async function teamCreatedSubmit() {
	let teamName =  document.getElementById("team_name").value;
	let fieldCountry = document.getElementById("stadium_country").value;
	let fieldState = document.getElementById("stadium_state").value;
	let fieldCity = document.getElementById("stadium_city").value;
	let fieldPostalCode = document.getElementById("stadium_postal_code").value;

	let response = "";
	try{
		response = await addTeamRequest(username_global,teamName,fieldCountry,fieldState,fieldCity,fieldPostalCode);
	if (response==null) {
		alert("server error");
	} 
	else {
		if (!(response.status=="true")) {
			alert("סליחה, מצטערים"); 
		}
		else {
			alert("Team \"" + teamName + "\" has been added successfully!");
		}
	}
	backToMenu();
	} catch(e){
		alert("server error");
		clearData();
		welcome();
	}
}

async function updateRankingPolicySubmit() {
	let league = document.getElementById("rp_league_name").value;
	let season = document.getElementById("rp_season").value;
	let wins = document.getElementById("wins_rank").value;
	let draws = document.getElementById("draws_rank").value;
	let goals = document.getElementById("goals_rank").value;
	let yellowCards = document.getElementById("yellow_cards_rank").value;
	let redCards = document.getElementById("red_cards_rank").value;

	let reqResult="";
	try{
		reqResult = await setRankingPolicyRequest(username_global,league,season,wins,goals,draws,yellowCards,redCards);
		if (reqResult==null) {
			alert("FATAL ERROR. SERVER DIDN'T RESPOND.");
		}
		else if (!reqResult.status=="true") {
			alert("Request denied. Please try again");
		}
		else {
			alert("Policy Updated Successfully!");
		}
		showActions();
	} catch(e){
		alert("server error");
		clearData();
		welcome();
	}
}

async function updateGameSettingPolicySubmit() {
	let league = document.getElementById("gsp_league_name").value;
	let season = document.getElementById("gsp_season").value;
	let gameSettingPolicy = document.getElementById("gameSettingPolicySelect").value;

	let reqResult = "";
	try{
		reqResult = await setGameSettingPolicyRequest(username_global,league,season,gameSettingPolicy);
		if (reqResult==null) {
			alert("FATAL ERROR. SERVER DIDN'T RESPOND.");
		}
		else if (!reqResult.status=="true") {
			alert("Request denied. Please try again");
		}
		else {
			alert("Policy Updated Successfully!");
		}
		showActions();
	}
	catch(e){
		alert("server error");
		clearData();
		welcome();
	}
}

async function gameReportSubmit() {
	let gameID = $("#game_to_report").val();
	let report = $("#report").val();

	let respone = "";
	try{
		respone = await createGameReportRequest(username_global, gameID, report);
		console.log(respone);
		if (respone == null){
			alert("server error")
		}
		else{
			if(!(respone.status=="true")){
				alert("Request denied. try again")
			}
			else{
				alert("Game report was created successfully")
			}
		}
		showActions();
	} catch(e){
		alert("server error");
		clearData();
		welcome();
	}
}

async function createEventSubmit() {
	let gameID = $("#gameID").val();
	let game_minute = $("#game_minute").val();
	let eventType = $("#eventTypeOption").val();
	let player = $("#playerUsername").val();
	let eventDescription = $("#eventDescription").val();

	let response = "";
	try{
		response = await addEventToGameRequest(username_global, gameID, game_minute, eventDescription, eventType, player);
		if (response == null){
			alert("server error")
		}
		else{
			if(!(response.status=="true")){
				alert("Request denied. try again")
			}
			else{
				alert("Game event was created successfully");
			}
		}
		showActions();
	} catch(e){
		alert("server error");
		clearData();
		welcome();
	}
	
}

function editEventSubmit() {
	alert("The Event Editted Successfully!");
	showActions();
}

function subscribedSubmit() {

	showActions();
}


function logout() {
	let ans = confirm("Are You Sure You Want To Leave?");
	if (ans) {
		welcome();
	}
}