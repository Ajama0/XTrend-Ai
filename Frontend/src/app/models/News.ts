export interface News{


    source ?: Source
    author ?:String
    title ?:String
    description ?: String
    url ?: String
    urlToImage ?: String 
    publishedAt ?: String
    content ?: String
    
   
}

export interface Source{
    id : String
    name : String
}