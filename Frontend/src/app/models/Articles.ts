export interface Articles{


    source ?: Source
    author ?:string
    title :string
    description : string
    url : string
    urlToImage : string 
    publishedAt ?: string
    content ?: string
  
   
}

export interface Source{
    id : string
    name : string
}