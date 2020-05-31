function welcome() {
	document.getElementById("main").style = "display: inline-block"
	//login reset
	document.getElementById("login").style = "display: inline-block"
	document.getElementById("logName").value = '';
	document.getElementById("logPswd").value = '';
	document.getElementById("welcome").style = "display: inline-block"
	document.getElementById("actions_title").style = "display: none"
	document.getElementById("actions").style = "display: none"
}

//windows
function manageGameWindow() {
	document.getElementById("actions_title").style = "display: none"
	document.getElementById("actions").style = "display: none"
	document.getElementById("manage_game").style = "display: inline-block"
	document.getElementById("manage_game_title").style = "display: inline-block"
	document.getElementById("manage_game_background").style = "display: inline-block"
}

function chooseLeaguePolicyWindow() {
	document.getElementById("actions_title").style = "display: none"
	document.getElementById("actions").style = "display: none"
	document.getElementById("league_policy").style = "display: inline-block"
	document.getElementById("league_policy_title").style = "display: inline-block"
	document.getElementById("league_policy_background").style = "display: inline-block"
}

function subscriptionWindow() {
	document.getElementById("container").style.backgroundImage = "url(https://images.daznservices.com/di/library/GOAL/7c/d1/cristiano-ronaldo-real-madrid-juventus-uefa-champions-league_18580w5nkl89y1tztc55jxqh7g.jpg?t=-1930245854&quality=100)"
	document.getElementById("actions_title").style = "display: none"
	document.getElementById("actions").style = "display: none"
	document.getElementById("subscription_background").style = "display: inline-block"
	document.getElementById("subscription_title").style = "display: inline-block"
	document.getElementById("subscription").style = "display: inline-block"
}


function createNewTeamWindow() {
	document.getElementById('team_name').value = '';
	document.getElementById('stadium_country').value = '';
	document.getElementById('stadium_state').value = '';
	document.getElementById('stadium_city').value = '';
	document.getElementById('stadium_postal_code').value = '';

	document.getElementById("new_team_background").style = "display: inline-block"
	document.getElementById("actions_title").style = "display: none"
	document.getElementById("actions").style = "display: none"
	document.getElementById("new_team").style = "display: inline-block"
	document.getElementById("new_team_title").style = "display: inline-block"
}

async function chooseRankingPolicyWindow() {
	document.getElementById('wins_rank').value = '';
	document.getElementById('draws_rank').value = '';
	document.getElementById('goals_rank').value = '';
	document.getElementById('yellow_cards_rank').value = '';
	document.getElementById('red_cards_rank').value = '';

	document.getElementById("actions_title").style = "display: none"
	document.getElementById("actions").style = "display: none"
	document.getElementById("ranking_policy_background").style = "display: inline-block"
	document.getElementById("ranking_policy_title").style = "display: inline-block"
	document.getElementById("league_ranking_policy").style = "display: inline-block"

	//adding leagues to select
	let leagueSelect = document.getElementById('rp_league_name');
	let allLeagues = await getLeagueNamesRequest();
	for (i=leagueSelect.options.length-1; i>=1; i--) { //making sure it's clean before adding
		leagueSelect.options[i]=null;
	}
	allLeagues.forEach(leagueElement => {
		leagueSelect.options[leagueSelect.options.length] = new Option(leagueElement,leagueElement);
	});
	//adding seasons for selected league
	leagueSelect.onchange = async function(){
		let seasonsForLeague = await getAllLeagueSeasons(leagueSelect.options[leagueSelect.selectedIndex].value);
		let seasonSelect = document.getElementById('rp_season');
		for (i=seasonSelect.options.length-1; i>=1; i--) { //making sure it's clean before adding 
			seasonSelect.options[i]=null;
		}
		seasonsForLeague.forEach(seasonElement=> {
			seasonSelect.options[seasonSelect.options.length] = new Option(seasonElement,seasonElement);
		});
	};
}

async function chooseGameSettingPolicyWindow() {
	document.getElementById("actions_title").style = "display: none"
	document.getElementById("actions").style = "display: none"
	document.getElementById("setting_policy_background").style = "display: inline-block"
	document.getElementById("setting_policy_title").style = "display: inline-block"
	document.getElementById("league_game_setting_policy").style = "display: inline-block"

	//adding leagues to select
	let leagueSelect = document.getElementById('gsp_league_name');
	let allLeagues = await getLeagueNamesRequest();
	for (i=leagueSelect.options.length-1; i>=1; i--) { //making sure it's clean before adding
		leagueSelect.options[i]=null;
	}
	allLeagues.forEach(leagueElement => {
		leagueSelect.options[leagueSelect.options.length] = new Option(leagueElement,leagueElement);
	});
	//adding seasons for selected league
	leagueSelect.onchange = async function(){
		let seasonsForLeague = await getAllLeagueSeasons(leagueSelect.options[leagueSelect.selectedIndex].value);
		let seasonSelect = document.getElementById('gsp_season');
		for (i=seasonSelect.options.length-1; i>=1; i--) { //making sure it's clean before adding
			seasonSelect.options[i]=null;
		}
		seasonsForLeague.forEach(seasonElement=> {
			seasonSelect.options[seasonSelect.options.length] = new Option(seasonElement,seasonElement);
		});
	};
}

//TODO delete this function?
function updateGameSettingPolicyWindow() {
	alert("The Laegue Policy Updated Successfully!");
	showActions();
}

function editGameEventWindow() {
	document.getElementById("manage_game").style = "display: none"
	document.getElementById("manage_game_title").style = "display: none"
	document.getElementById("edit_game_event_title").style = "display: inline-block"
	document.getElementById("edit_game_event").style = "display: inline-block"
}

async function createGameEventWindow() {

	// getting the active game of the referee
	let gameSelect = document.getElementById('gameID');
	document.getElementById('game_minute').value = '';
	document.getElementById('eventDescription').value = '';

	let playerSelect = document.getElementById('playerUsername');
	for(i=1; i<playerSelect.length; i++){
		playerSelect.options[i] = null;
	}
	let activeGame = await getRefereeActiveGameRequest(username_global);
	if (activeGame.gameID == null){
		alert("You don't have active game");
		backToMenu();
	}
	else{
		for (i=gameSelect.options.length-1; i>=1; i--) { //making sure it's clean before adding
			gameSelect.options[i]=null;
		}
		gameSelect.options[gameSelect.options.length] = new Option(activeGame.hostTeamName+" Vs. "+activeGame.guestTeamName, activeGame.gameID);
		let hostTeamPlayers = await getAllTeamPlayersRequest(activeGame.hostTeamName);
		let guestTeamPlayers = await getAllTeamPlayersRequest(activeGame.guestTeamName);
		hostTeamPlayers.forEach(hostPlayer => {
			playerSelect.options[playerSelect.options.length] = new Option(activeGame.hostTeamName+": "+hostPlayer, hostPlayer);
		});
		guestTeamPlayers.forEach(guestPlayer => {
			playerSelect.options[playerSelect.options.length] = new Option(activeGame.guestTeamName+": "+guestPlayer, guestPlayer);
		})
		document.getElementById("manage_game").style = "display: none"
		document.getElementById("manage_game_title").style = "display: none"
		document.getElementById("new_game_event_title").style = "display: inline-block"
		document.getElementById("new_game_event").style = "display: inline-block"
	}
}

async function writeGameReportWindow() {
	// getting the active report game of the referee
	let gameSelect = document.getElementById('game_to_report');
	document.getElementById('report').value = '';
	let activeReportGame = await getRefereeReportActiveGame(username_global);
	if(activeReportGame.gameID == null){
		alert("You don't have active game to write a report to");
		backToMenu();
	}
	else{
		for (i=gameSelect.options.length-1; i>=1; i--) { //making sure it's clean before adding
			gameSelect.options[i]=null;
		}
		gameSelect.options[gameSelect.options.length] = new Option(activeReportGame.hostTeamName+" Vs. "+activeReportGame.guestTeamName, activeReportGame.gameID);
		document.getElementById("manage_game").style = "display: none"
		document.getElementById("manage_game_title").style = "display: none"
		document.getElementById("manage_game_background").style = "display: inline-block"
		document.getElementById("write_game_report_title").style = "display: inline-block"
		document.getElementById("write_game_report").style = "display: inline-block"
	}
}

async function logoutWindow() {
	let ans = confirm("Are You Sure You Want To Leave?");
	if (ans) {
		// clear all previos session content
		try{
			let response = await logoutRequest(username_global);
		} catch(e){

		}
		clearData();
		welcome();
	}
}

function clearData(){
	username_global = "";
	roles_global = [];
	if (stompClient != null){
		stompClient.disconnect();
	}
}




//menus by role
function owner_menu() {
	document.getElementById("new_team_col").style = "display: inline-block"
}

function referee_menu() {
	document.getElementById("manage_game_col").style = "display: inline-block"
}

function agent_menu() {
	document.getElementById("league_ranking_policy_col").style = "display: inline-block"
	document.getElementById("league__game_setting_policy_col").style = "display: inline-block"
}


function clearAllDisplay() {
	document.getElementById("login").style = "display: none"
	document.getElementById("welcome").style = "display: none"
	document.getElementById("manage_game").style = "display: none"
	document.getElementById("manage_game_title").style = "display: none"
	document.getElementById("manage_game_background").style = "display: none"
	document.getElementById("new_team").style = "display: none"
	document.getElementById("new_team_title").style = "display: none"
	document.getElementById("new_team_background").style = "display: none"
	document.getElementById("subscription").style = "display: none"
	document.getElementById("subscription_title").style = "display: none"
	document.getElementById("subscription_background").style = "display: none"
	document.getElementById("ranking_policy_background").style = "display: none"
	document.getElementById("ranking_policy_title").style = "display: none"
	document.getElementById("league_ranking_policy").style = "display: none"
	document.getElementById("setting_policy_background").style = "display: none"
	document.getElementById("setting_policy_title").style = "display: none"
	document.getElementById("league_game_setting_policy").style = "display: none"
	document.getElementById("login").style = "display: none"
	document.getElementById("welcome").style = "display: none"
	document.getElementById("write_game_report_title").style = "display: none"
	document.getElementById("write_game_report").style = "display: none"
	document.getElementById("new_game_event_title").style = "display: none"
	document.getElementById("new_game_event").style = "display: none"
	document.getElementById("edit_game_event_title").style = "display: none"
	document.getElementById("edit_game_event").style = "display: none"

	//cols
	document.getElementById("new_team_col").style = "display: none"
	document.getElementById("league_ranking_policy_col").style = "display: none"
	document.getElementById("league__game_setting_policy_col").style = "display: none"
	document.getElementById("manage_game_col").style = "display: none"

}


function backToMenu() {
	showActions();
}
