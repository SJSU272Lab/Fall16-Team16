'use strict';

var rpslsAppInfo = angular.module('rpslsApp.info', [])

   /**
    * Info Manager
    */

   .service( 'infoManager', function() {
      var modals = [
         { id: 'author', title: 'Who Made This?', url: 'info/author.html' },
         { id: 'colophon', title: ' Reference', url: 'info/reference.html' },
         { id: 'wtf', title: 'What is ZeroSum?', url: 'info/wtf.html' }

      ];

      // Get the array of player data
      this.getModalContent = function() {
         return modals;
      }
   })

   /**
    * Modal Info
    */

   .controller( 'infoController', ['infoManager', function(infoManager) {
      this.modalContent = infoManager.getModalContent();
   }]);
