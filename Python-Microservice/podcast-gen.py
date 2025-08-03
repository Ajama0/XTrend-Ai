from dotenv import load_dotenv
from podcastfy.client import generate_podcast
from IPython.display import Audio, display
import os
from flask import Flask, request, jsonify
from flask_cors import CORS
from flask import send_from_directory
from flask import send_file



app = Flask(__name__)
CORS(app)
load_dotenv()
#Also Introduce the podcast as Rela A I, pronouncing each letter in A-I clearly

trending_news_config = {
    'conversation_style': ['Engaging', 'Fast-paced'],
    'roles_person1': 'enthusiastic host who makes the news fun and interesting',
    'roles_person2': 'fun, sarcastic co-host who reacts with jokes and surprise',
    'podcast_name': 'Rela A-I',
    'podcast_tagline': 'Your daily dose of trending news',
    'output_language': 'English',
    'user_instructions': 'do not spell out podcast name, read it normally. Speakers should often overlap in their sentences. Make sure to include a lot of humour and quotes from the articles.',
    'engagement_techniques': ['Quotes', 'Humour'],
    'creativity': 1,
}



@app.route('api/v1/podcast/create/news', methods=['POST'])
def generate():
    print("in function -----------------")
    print("Content-Type:", request.content_type)
    print("Raw data:", request.data)




    requestObject = request.get_json()

    print("requestObject", requestObject)
    if not requestObject:
        return jsonify({"error": "Invalid request data"}), 400
    
    url = requestObject.get("articleUrl")
    contentFormat = requestObject.get("contentForm")

    print("url", url)
    print("contentFormat", contentFormat)
    
    
    longform = contentFormat == "LONG"

    OPENAI_KEY = os.getenv("OPENAI_API_KEY")
    audio_path = generate_podcast(
        urls=[url],
        tts_model="openai",
        longform=longform,
        conversation_config=trending_news_config,
        api_key_label=OPENAI_KEY
    )


    print("audio path", audio_path)
    if not audio_path:
        return jsonify({"error": "Failed to generate podcast"}), 500
    
    
     #we can return the audio file directly
    return send_file(audio_path, mimetype='audio/mpeg')



    

@app.route('/test', methods=['POST'])
def testing():
    print("in testing function")
    print("data",request.data)
    print ("content_type" ,request.content_type)

    

@app.route('/audio/<filename>', methods=['GET'])
def serve_audio(filename):
    return send_from_directory("data/audio", filename)

if __name__ == '__main__':
    app.run(port=8000, debug=True)


