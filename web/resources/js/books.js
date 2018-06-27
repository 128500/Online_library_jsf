
    function showLoading(data){
        if(data.status === "begin"){
           document.getElementById('loading').style.display = "block";
           document.getElementById('books_content_panel').style.display = "none";
           
        }
        else if(data.status === "success"){
            document.getElementById('loading').style.display = "none";
            document.getElementById('books_content_panel').style.display = "block";
        } 
    }
