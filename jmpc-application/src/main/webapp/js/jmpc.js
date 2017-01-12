angular.module('jmpcApp', [])
  .controller('SongListController', function() {
    var songList = this;

    songList.songs = [
    	{
    		title: 'Midnight at the Lost and Found',
    		artist: 'Meatloaf'
    	},
    	{
    		title: 'Paradise by the Dashboard Light',
    		artist: 'Meatloaf'
    	}
    ];

  });
