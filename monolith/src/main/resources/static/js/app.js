/**
 * Enables and renders notifications.
 *
 * @param {string} msg message to display
 */
function notify(msg) {
  if (Notification.permission === 'granted') {
    new Notification(msg);
  } else if (Notification.permission !== 'denied') {
    Notification.requestPermission(function (permission) {
      if (permission === 'granted') {
        new Notification(msg);
      }
    });
  }
}

function status(response) {
  if (response.status >= 200 && response.status < 300) {
    return Promise.resolve(response)
  } else {
    return Promise.reject(new Error(response.statusText))
  }
}

function json(response) {
  return response.json()
}

function getId(response) {
  var location = response.headers.get('Location');

  if (location) {
    return Promise.resolve(location.substring(location.lastIndexOf('/') + 1))
  } else {
    return Promise.reject(new Error("Location header missing!"))
  }
}

/**
 * Create an account
 */
function createAccount(e) {
  e.preventDefault();

  var headers = new Headers();
  headers.append("Content-Type", "application/json");

  fetch('/account', {
    method: 'post',
    headers: headers,
    credentials: 'include',
    body: JSON.stringify({
      balance: parseInt(e.target.balance.value, 10)
    })
  })
    .then(status)
    .then(getId)
    .then(function (id) {
      notify('Account ' + id + ' created!');
      // pre-fill the lookup input
      document.getElementById('accountId').value = id;
    }).catch(alert);
}

function getAccount(e) {
  e.preventDefault();

  fetch('/account/' + e.target.accountId.value, {credentials: 'include'})
    .then(status)
    .then(json)
    .then(function (json) {
      document.getElementById('accountJSON').innerText = JSON.stringify(json, undefined, 2);
    }).catch(alert);
}

/**
 * Create Transaction
 */
function createTransaction(e) {
  e.preventDefault();

  var headers = new Headers();
  headers.append("Content-Type", "application/json");

  fetch('/transaction', {
    method: 'post',
    headers: headers,
    credentials: 'include',
    body: JSON.stringify({
      source: e.target.source.value,
      target: e.target.target.value,
      amount: parseInt(e.target.amount.value, 10)
    })
  }).then(status)
    .then(getId)
    .then(function (id) {
      notify('Transaction ' + id + ' created!');
      // pre-fill the lookup input
      document.getElementById('transactionId').innerText = id;
    }).catch(alert);
}

function getTransaction(e) {
  e.preventDefault();

  fetch('/transaction/' + e.target.transactionId.value, {credentials: 'include'})
    .then(status)
    .then(json)
    .then(function (json) {
      document.getElementById('transactionJSON').innerText = JSON.stringify(json, undefined, 2);
    }).catch(alert);
}

// simple router based on the request hash
var hash;

/**
 * swap the visible div
 */
function swapContent() {
  // hide the previously visible div
  if (hash) {
    document.getElementById(hash).style.display = 'none';
  }
  // make the newly selected hash visible
  hash = window.location.hash.split('#')[1] || 'account';
  document.getElementById(hash).style.display = 'block';
}

// install listener for hash changes
window.onhashchange = swapContent;

// default to account
swapContent();
