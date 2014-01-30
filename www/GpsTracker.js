var GpsTracker = function(){};

GpsTracker.NATIVE_CLASS = 'org.apache.cordova.gps.GpsTracker';

GpsTracker.prototype.startTracking = function(successCallback, failureCallback, movementThreshold)
{
	cordova.exec(successCallback, failureCallback, GpsTracker.NATIVE_CLASS, 'startTracking', [movementThreshold]);
};

GpsTracker.prototype.stopTracking = function(successCallback, failureCallback)
{
	cordova.exec(successCallback, failureCallback, GpsTracker.NATIVE_CLASS, 'stopTracking', []);
};

GpsTracker.prototype.isTracking = function(successCallback, failureCallback)
{
	cordova.exec(successCallback, failureCallback, GpsTracker.NATIVE_CLASS, 'isTracking', []);
};

module.exports = new GpsTracker();