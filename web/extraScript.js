function view() {
    var mainList = document.getElementsByClassName('menu');
    var all = '';
    for (var i = 0; i < mainList.length; i++) {
        var title = mainList[i];
        var tMenu = title.querySelector('.title').innerHTML;
        all += '<li>' + tMenu + ' (';
        var items = title.getElementsByTagName('li');
        for (var t = 0; t < items.length; t++) {
            all += items[t].innerHTML;
            if (t != items.length-1)
                all += ', ';
        }
        all += ')</li>';
    }
    document.getElementById('demo').innerHTML = all;
}