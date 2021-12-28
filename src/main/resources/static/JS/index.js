let currentPage = document.getElementById("curr-page");
if (currentPage.innerHTML == "1") {
  document.getElementById("prev-btn").disabled = true;
}else if(currentPage.innerHTML == "last" && new URLSearchParams(window.location.search).get("start") == "0"){
  document.getElementById("prev-btn").disabled = true;
  document.getElementById("next-btn").disabled = true;
}else if(currentPage.innerHTML == "last"){
  document.getElementById("next-btn").disabled = true;
}

function searchPost() {
  var el = document.getElementById("search");
  el.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
      let searchedValue = event.target.value;
      let url = new URL(window.location.origin);
      window.location.href = url.origin+"?start=0&limit=10&search="+searchedValue;
    }
  });
}

function sortingPost(sortingBtn) {
      var el = document.getElementById(sortingBtn.id);
      console.log(el);
      let order = el.value;
      let url = new URL(window.location);
      url.searchParams.set("sortField" , "publishedAt");
      url.searchParams.set('order', order);
      window.location.href = url;
  }

function prevPage(buttonElement) {
  let params = new URLSearchParams(window.location.search);
  let start = params.get("start");
  let limit = params.get("limit");
 
    let newStart = parseInt(start)-10;
    let newLimit = 10;
    let url = new URL(window.location);
    url.searchParams.set("start" , newStart);
    url.searchParams.set('limit', newLimit);
    window.location.href = url;
}

function nextPage(buttonElement) {
  let params = new URLSearchParams(window.location.search);
  let start = params.get("start");
  let limit = params.get("limit");
  
  let newStart = parseInt(start) + 10;
  let newLimit = 10;
  let url = new URL(window.location);
      url.searchParams.set("start" , newStart);
      url.searchParams.set('limit', newLimit);
  window.location.href = url;
}

function filterPostByAuthor(authorCheckbox) {
  let author = authorCheckbox.value;
  
  if(authorCheckbox.checked == true){
    let url = new URL(window.location);
    url.searchParams.append("author", author);
    window.location.href = url;
  }else{
    let url = new URL(window.location);
    urlParams=removeParam(url.search, "author", author);
    window.location.href = url.origin+urlParams;
  }
}

function filterPostByTag(tagCheckbox) {
  let tag = tagCheckbox.value;
  
  if(tagCheckbox.checked == true){
    let url = new URL(window.location);
    url.searchParams.append("tagId", tag);
    window.location.href = url; 
  }else{
    let url = new URL(window.location);
    urlParams=removeParam(url.search, "tagId", tag);
    
    window.location.href = url.origin+urlParams;
  }
}

function removeParam(sourceURL, key, value) {
  var params = sourceURL.replace('?','').split('&');
  let removeParam = key+"="+value;
  params = params.filter(param => param !== removeParam);
  return '/?'+params.join('&');
}

function selectPublishedDate(){
  let startDate = document.getElementById("startDate").value;
  let endDate = document.getElementById("endDate").value;
  let url = new URL(window.location);
  url.searchParams.set("fromDate" , startDate);
  url.searchParams.set("toDate" , endDate);
  window.location.href = url;
}