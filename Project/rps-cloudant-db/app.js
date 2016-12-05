/**
 * Module dependencies.
 */

var express = require('express'),
    http = require('http'),
    path = require('path'),
    fs = require('fs');

var app = express();

var db;

var cloudant;

var dbCredentials = {
    dbName: 'rockpaperscissordb'
};

var bodyParser = require('body-parser');
var methodOverride = require('method-override');
var logger = require('morgan');
var errorHandler = require('errorhandler');
var multipart = require('connect-multiparty')

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);
app.use(logger('dev'));
app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(bodyParser.json());
app.use(methodOverride());
app.use(express.static(path.join(__dirname, 'public')));
app.use('/style', express.static(path.join(__dirname, '/views/style')));

// development only
if ('development' == app.get('env')) {
    app.use(errorHandler());
}

function initDBConnection() {
/*
		//When running on Bluemix, this variable will be set to a json object
    //containing all the service credentials of all the bound services
    if (process.env.VCAP_SERVICES) {
        var vcapServices = JSON.parse(process.env.VCAP_SERVICES);
        // Pattern match to find the first instance of a Cloudant service in
        // VCAP_SERVICES. If you know your service key, you can access the
        // service credentials directly by using the vcapServices object.
        for (var vcapService in vcapServices) {
            if (vcapService.match(/cloudant/i)) {
                dbCredentials.url = vcapServices[vcapService][0].credentials.url;
            }
        }
    } else { //When running locally, the VCAP_SERVICES will not be set

        // When running this app locally you can get your Cloudant credentials
        // from Bluemix (VCAP_SERVICES in "cf env" output or the Environment
        // Variables section for an app in the Bluemix console dashboard).
        // Alternately you could point to a local database here instead of a
        // Bluemix service.
        // url will be in this format: https://username:password@xxxxxxxxx-bluemix.cloudant.com
        dbCredentials.url = "https://799d1774-1866-4be9-9df9-88dadb413617-bluemix:f87e1020edfda16df0b20199c612da70ecaa5db6658efa65ce0a18e823996312@799d1774-1866-4be9-9df9-88dadb413617-bluemix.cloudant.com";
    }
*/

		dbCredentials.url = "https://799d1774-1866-4be9-9df9-88dadb413617-bluemix:f87e1020edfda16df0b20199c612da70ecaa5db6658efa65ce0a18e823996312@799d1774-1866-4be9-9df9-88dadb413617-bluemix.cloudant.com";
    cloudant = require('cloudant')(dbCredentials.url);

    // check if DB exists if not create
    cloudant.db.create(dbCredentials.dbName, function(err, res) {
        if (err) {
            console.log('Could not create new db: ' + dbCredentials.dbName + ', it might already exist.');
        }
    });

    db = cloudant.use(dbCredentials.dbName);
}

initDBConnection();

function createResponseData(matchString) {

    var responseData = {
        match: matchString
    };
    return responseData;
}

app.get('/api/match', function(request, response) {

	console.log("Get method invoked.. ")

	db = cloudant.use(dbCredentials.dbName);
	const params = {include_docs: true };

	db.list(params, function(err, body) {
		if (!err) {
			var len = body.rows.length;
			console.log('total # of docs -> ' + len);
			var matchString = "";
			body.rows.forEach(function(document) {
				if (document.doc && document.doc.match) {
					matchString += document.doc.match;
				}
			});
			var responseData = createResponseData(matchString);
			response.json(responseData);
		} else {
			response.json(err);
		}
	});
});

app.get('/api/all', function(request, response) {

	console.log("Get method invoked.. ")

	db = cloudant.use(dbCredentials.dbName);
	const params = {include_docs: true };

	db.list(params, function(err, body) {
		if (!err) {
			var len = body.rows.length;
			console.log('total # of docs -> ' + len);
			var matchString = "";
			response.json(body);
		} else {
			response.json(err);
		}
	});
});


http.createServer(app).listen(app.get('port'), '0.0.0.0', function() {
    console.log('Express server listening on port ' + app.get('port'));
});
