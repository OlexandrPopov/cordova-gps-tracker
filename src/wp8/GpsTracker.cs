using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WPCordovaClassLib.Cordova;
using WPCordovaClassLib.Cordova.Commands;
using WPCordovaClassLib.Cordova.JSON;

namespace org.apache.cordova.gps
{
    class GpsTracker: BaseCommand
    {
        private bool mIsTracking = false;
        private string mCallback;
        private GeoCoordinateWatcher mCoordinateWatcher = new GeoCoordinateWatcher(GeoPositionAccuracy.High);

        public void startTracking(string options)
        {
            string[] args = JsonHelper.Deserialize<string[]>(options);
            int movementThreshold = Int32.Parse(args[0]);
            mCallback = args[args.Length - 1];
            mCoordinateWatcher.MovementThreshold = movementThreshold;
            mCoordinateWatcher.PositionChanged += onPositionChanged;
            mCoordinateWatcher.Start();
            mIsTracking = true;
        }

        public void stopTracking(string options)
        {
            mCoordinateWatcher.PositionChanged -= onPositionChanged;
            mCoordinateWatcher.Stop();
            mIsTracking = false;
            DispatchCommandResult(new PluginResult(PluginResult.Status.OK));
        }

        public void isTracking(string options)
        {
            DispatchCommandResult(new PluginResult(PluginResult.Status.OK, mIsTracking));
        }

        private void onPositionChanged(object sender, GeoPositionChangedEventArgs<GeoCoordinate> e)
        {
            if(!string.IsNullOrEmpty(mCallback))
            {
                PluginResult result = new PluginResult(PluginResult.Status.OK, buildJsResult(e.Position.Location.Latitude, e.Position.Location.Longitude));
                result.KeepCallback = true;
                DispatchCommandResult(result, mCallback);
            }
        }

        private string buildJsResult(double latitude, double longitude)
        {
            return "{\"latitude\":" + latitude + ",\"longitude\":" + longitude + "}";
        }

    }
}
