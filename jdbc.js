window.jdbc = {};

/*******************************************************************************
 * window.jdbc.open(
 * 		{ 	url : 'jdbc:mysql://server/database', 
 * 			user : 'user',
 * 			password : 'pass', 
 * 			class : 'com.mysql.jdbc.Driver' 
 * 		}, function() {
 * 			alert('ok');
 * 		}, function() { 
 * 			alert('error'); 
 * 		});
 */

window.jdbc.open = function(str, success, error) {
	cordova.exec(success, error, "Jdbc", "open", [ str ]);
};

/*******************************************************************************
 * window.jdbc.executeQuery({ query : "select * from x" }, 
 * 		function(result) {
 * 			alert("ok"); 
 * 			console.log(result); 
 * 		}, function(error) { 
 * 			alert(error);
 * 			window.jdbc.close(); 
 * 		});
 */
window.jdbc.executeQuery = function(str, success, error) {
	cordova.exec(success, error, "Jdbc", "executeQuery", [ str ]);
};

/*******************************************************************************
 * window.jdbc.close(
 * 		function() {
 * 			console.log('Connection Closed.');
 * 		}, function(error) {
 * 			alert(error)
 * 		});
 */

window.jdbc.close = function(success, error) {
	cordova.exec(success, error, "Jdbc", "close", []);
};
