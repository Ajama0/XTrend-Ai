export interface PodcastResponse{
    podcastId:number;
    key?:string;
    status?:string;

    url?:string

    imageUrl?:string
    description?:string
    country?:Array<string>
    category?:Array<string>

    podcastTagline?:string
    createdAt?:string

}