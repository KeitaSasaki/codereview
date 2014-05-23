/*
 * Photoviewer
 *
 */
/*
 * Flickr
 *
 */
(function($, window, document) {

    //一度JSONから取得した画像はキャッシュしておく
    var photoArray = null;
    var $win = $(window);

    //0: 初期値, 1: Landscape mode, 2: Portrait mode
    var windowState = 0;
    const winLS = 1, winPR = 2;
    var body = $('body');

    //Landscape mode時の現在表示されている画像のインデックス。-1は非表示を表す
    var FSPhotoIndex = -1;

    const LSDefaultImageName = 'Kasumi Arimura';
    const PRDefaultImageName = 'Kanna Hashimoto';




    //初期化
    $(function() {
        //拡大画像を表示する場所を用意
        setOverlay();

        setWindowsStyle();
        //resizeしたらウィンドウスタイルを変更
        $win.on('resize', setWindowsStyle);
        prepareEnlargeImage();

        //検索ボタンのイベントハンドラ
        $('#searchButton').on('click', function(){
            var text = $('#searchText').val();
            console.log(text);
            searchAndDisplay(text);
        });

        $('#result').on('click', '.largePhoto', nextFullScreenPhoto);



            }
     );

    //現在のウィンドウのサイズを見て、モードの切り替え
    function setWindowsStyle(){
        var wi = $win.width();
        var he = $win.height();
        if(wi > he && windowState != winLS){
            // $('h1').text('Landscape Mode');
            console.log('landscape mode.');
            windowState = winLS;
            body.removeClass('Portrait');
            body.addClass('Landscape');
            switchLandscapeMode();
        } else if(wi < he && windowState != winPR){
            // $('h1').text('Portrait Mode');
            console.log('portrait mode.');
            windowState = winPR;
            body.removeClass('Landscape');
            body.addClass('Portrait');
            switchPortraitMode();
        }
    };

    //wordを使って検索
    //検索したらdefer.promiseを返す
    function searchByWord(word){
        var options = {};
        options.url = 'https://api.flickr.com/services/rest/';
        options.method = 'GET';
        options.params = {
            method: 'flickr.photos.search',
            per_page: 30,
            text: word,
            api_key: 'd584a6eba3296ba0b96d40aae6d82e2b',
            // api_key: 'd0e8f2363e848dc5c0316340f98c0bce',

            format: 'json',
            nojsoncallback: 1
        };
        return requestSearch(options);
    }


    function requestSearch(options) {
        var defer = $.Deferred();
        $.ajax({
            type: options.method,
            url: options.url,
            data: options.params
        })
        .done(function(data) {
            console.log(data.photos.photo);
            photoArray = data.photos.photo;
            if(photoArray.length === 0) photoArray = null;
            // switchPortraitMode();
            // updatePhotoDisplay();
            defer.resolve();
        })
        .fail(function() {
            console.log("失敗");
            defer.reject();
        });
        return defer.promise();
    };

    //グローバル変数photoArrayの画像を全て表示する
    function updatePhotoDisplay(){        //画像一覧を表示
        //現在表示されている画像を削除
        $('#result img').remove();
        var tag = "";
        $(photoArray).each(function(index) {
            var file = "http://farm" + this.farm + ".static.flickr.com/" + this.server + "/" + this.id + "_" + this.secret + "_" + "m" +".jpg";
            tag += "<img class='thumbnail' src='" + file + "' width='100' height='100'>";
        });
        $('#result').append(tag);
    }

    //Portrait modeに切り替わった際に呼ばれる
    function switchPortraitMode(){
        finishLandscapeMode();

        $('#searchForm').show();
        // prepareEnlargeImage();

        //始めはこれを表示しておく
        if(photoArray == null) searchAndDisplay(PRDefaultImageName);
        else updatePhotoDisplay();
    };

    function searchAndDisplay(text){
        searchByWord(text)
            .done(function(){updatePhotoDisplay();})
            .fail();
    }

    //portrait mode終了時に呼ぶ
    function finishPortraitMode(){
        //画像全消去
        $('#result img').remove();
        //overlayのDivを隠す
        $('.overlay').hide();

        //検索画面を隠す
        $('#searchForm').hide();
        // $('#result').off('click', '.thumbnail');
    };


    //LandScape modeに切り替わった際に呼ばれる
    function switchLandscapeMode(){
        finishPortraitMode();


        //とりあえず0番目？
        displayFullScreenPhoto(0);
    };

     //portrait mode終了時に呼ぶ
    function finishLandscapeMode(){
        $('#result img').remove();
        // $('#result').off('click', '.largePhoto');
        // $('#result').off('img');
        FSPhotoIndex = -1;

    };

    //配列のn番目を表示する。nが配列長を超えていたら0番目
    function displayFullScreenPhoto(n){
        console.log('fullScreenPhoto invoked.', n);


        if(photoArray == null){
            //適当に検索
            searchByWord(LSDefaultImageName)
                .done(function(){
                    _displayFullScreenPhoto(n);
                    console.log('full screen search OK.');
                })
            .fail(function(){console.log('full screen search fail.')});
        } else{
            _displayFullScreenPhoto(n);

        }

    }

    function _displayFullScreenPhoto(n){
        if(photoArray.length <= n) n = 0;
        FSPhotoIndex = n;
        var photo = photoArray[n];
        console.log(photo);
        var photoUrl = "http://farm" + photo.farm + ".static.flickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret + "_" + "m" +".jpg";
        var photoTag = "<img class='largePhoto' src='" + photoUrl + "' width='100%' height='100%'>";
        $('#result').append(photoTag);
        console.log(photoUrl);

    }

    function nextFullScreenPhoto(){
        $('#result img').remove();
        displayFullScreenPhoto(FSPhotoIndex + 1);
    }


    //拡大画像を表示する場所を用意
    function setOverlay(){
            //画像拡大のためのoverlayクラスを持つdivを設置
            $("body").prepend('<div class="overlay"></div>');
            var h = $(document).height();
            $(".overlay").css('height', h);

            //拡大画像をクリックしたら非表示にする
            $(".overlay").click(function() {
                $(".overlay").hide();
            });

//spanがカーソルについてまわるようにする
                $("body").mousemove(function(e) {
                    var x = e.pageX + 10;
                    var y = e.pageY - 30;
                    $("#cursol").css({top: y, left: x});
                });

    };


    //for 画像拡大
    // 参考：http://www.finefinefine.jp/web/kiji1758/
    function prepareEnlargeImage(){
        $(function() {
            // console.log('function for image enlargement invoked.');
            var thumbnails = $('.thumbnail');
            //画像アイコンにタップイベントを追加
            $('#result').on('click', '.thumbnail', function(){
                console.log(this);
                thumbnails.removeClass('clickedThumbnail');
                $(this).addClass('clickedThumbnail');
            });

            //サムネイルをクリックしたら画像が拡大されるようにする
            $('#result').on('click', '.thumbnail', function(){
                // console.log('thumbnail func invoked.');
                var url = $(this).attr('src');
                //拡大して表示するため、URLを加工して大きな画像の取得を試みる
                url = cutUnderM($(this).attr('src'));

                //画像の表示
                $(".overlay").empty().append('<img src="' + url + '" /><span id="cursol">画面クリックで閉じる</span>').fadeIn('fast');

                //画像位置の調整
                var w = ($(document).width()/2)-400;
                var t = $(document).scrollTop()+100;
                $(".overlay img").css({left: w, top: t, opacity: '1'});


            });
        });
    };


}(window.jQuery, window, document));




/*
   imageUrlが..._m.jpgだったら_mを切り取ったURLを返す
   */
function cutUnderM(imageUrl){
    //最後が_m.jpgだったら
    if(imageUrl.match(/_m\.jpg$/) !== null){
        console.log('match!', imageUrl);
        return imageUrl.substr(0, imageUrl.length - "_m.jpg".length) + '.jpg';
    }
    //フォーマットが違ったらそのまま返す
    return imageUrl;
}
