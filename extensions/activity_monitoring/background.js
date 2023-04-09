// Background script that listens for tab updates
chrome.tabs.onUpdated.addListener(function(tabId, changeInfo, tab) {
    // Only send the URL when the tab is fully loaded
    if (changeInfo.status == 'complete') {
      // Get the URL of the current tab
      var url = tab.url;
  
      // Send the URL to the Flask app using a POST request
      var xhr = new XMLHttpRequest();
      xhr.open('POST', 'http://127.0.0.1:5000/log_url');
      xhr.setRequestHeader('Content-Type', 'application/json');
      xhr.send(JSON.stringify({url: url}));
    }
  });
  