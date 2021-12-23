let currentPage = document.getElementById("curr-page");
if (currentPage.innerHTML == "1") {
  document.getElementById("prev-btn").disabled = true;
}
if(currentPage.innerHTML == "last"){
  document.getElementById("next-btn").disabled = true;
}

function searchPost() {
  var el = document.getElementById("search");
  el.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
      let searchedValue = event.target.value;
      window.location.href = "/search/" + searchedValue;
    }
  });
}

function prevPage(buttonElement) {
  let params = new URLSearchParams(window.location.search);
  let start = params.get("start");
  let limit = params.get("limit");
  
  let newStart = parseInt(start - 4);
  let newLimit = 4;
  window.location.href =
    "/?" + "start=" + newStart + "&limit=" + newLimit;
}

function nextPage(buttonElement) {
  let params = new URLSearchParams(window.location.search);
  let start = params.get("start");
  let limit = params.get("limit");

  let newStart = parseInt(start) + 4;
  let newLimit = 4;
  window.location.href =
    "/?" + "start=" + newStart + "&limit=" + newLimit;
}