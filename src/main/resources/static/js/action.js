function showAlert() {
    alert("The button was clicked!");
}

function showName(name) {
    alert("Here's the name: " + name);
}

function changePageSize() {
      $("#itemSizeForm").submit();
}


function setSelectValue (id, val) {
    document.getElementById(id).value = val;
}