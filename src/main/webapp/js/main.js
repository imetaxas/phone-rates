var currencies = ['EUR', 'USD', 'GBP', 'JPY', 'AUD', 'CHF', 'ZAR', 'CAD'];
var curr_len = currencies.length;
var curr_url = 'https://query.yahooapis.com/v1/public/yql?q=';
var active;
var all_currs = {};

$( '.menu-button' ).click(function () {
 if ( $( ".wrapper" ).is( ":hidden" ) ) {
  $( ".wrapper" ).slideDown("slow");
   } else {
    $( ".wrapper" ).slideUp("slow");
  }
  $(".fa-exchange, .fa-close").toggleClass("fa-exchange").toggleClass("fa-close");
});

//Dropdown expanding functionality
$('.currencies').hover(function() {
  $(this).addClass('open');
}, function() {
  $(this).removeClass('open');
});

$('.currencies li').bind('touchstart touchend', function(e) {
  e.preventDefault();
  $(this).addClass('open');
}, function() {
  $(this).removeClass('open');
});

//change selected currency
$('.currencies li').click(function() {
  $('.active').removeClass('active');
  $('.currencies').prepend($(this)).removeClass('open');
  $(this).addClass('active');
  hideDublicate();
  updateAmounts();
});

//Live update amounts
$('.amount').bind('keyup', function() {
  updateAmounts();
});

//Hides result same with the selected currency
function hideDublicate() {
  $('.hide').removeClass('hide');
  active = $('.currencies .active').attr('class').replace(/ active/, '');
  $('.results .' + active).addClass('hide');
}

function getRate(active, current) {
  if(current == active){
    return 1;
  }
  var name = active + current;
  var reverse_name = current + active;

  if (all_currs[name]) {
    return all_currs[name].Rate
  } else {
    return 1 / all_currs[reverse_name].Rate;
  }
}

function updateAmounts() {
  var userInput = $('.amount').val() || 0;
  for (var i = 0; i < curr_len; i++) {
    var amount = userInput * getRate(active, currencies[i]);
    $('.' + currencies[i] + ' .result').text(Number(amount).toFixed(3))
  }
}

function pairwise(list) {
  if (list.length < 2) {
    return [];
  }
  var first = list[0],
    rest = list.slice(1),
    pairs = rest.map(function(x) {
      return [first, x];
    });
  return pairs.concat(pairwise(rest));
}

//retrieves rates and stores them to all_currs object
$( document ).ready(function() {
  var query_uri_component = encodeURIComponent("select * from yahoo.finance.xchange where pair in");
  var all_currency_pairs = pairwise(currencies).map(function(arr) {
    return arr.join("")
  }).join('","');
  var request_url = curr_url + query_uri_component + encodeURIComponent(" (\"" + all_currency_pairs + "\")") + '&format=json&diagnostics=true&env=' + encodeURIComponent("store://datatables.org/alltableswithkeys") + '&callback=set_currs';
  console.log(request_url);
  $.ajax({
    dataType: 'jsonp',
    url: request_url,
    success: function( data ) {
      updateAmounts();
    },
    error: function(jqXHR, textStatus, errorThrown) {
      updateAmounts();
    }
  })
});

hideDublicate();