from dotenv import load_dotenv
from podcastfy.client import generate_podcast
from IPython.display import Audio, display
import os
from flask import Flask, request, jsonify
from flask_cors import CORS
from flask import send_from_directory
from flask import send_file



trending_news_config= {
    'conversation_style': ['Engaging', 'informative', 'Enthusiastic', 'Educational'], 
    'roles_person1': 'main summarizer', 
    'roles_person2': 'Subject matter expert', 
    'dialogue_structure': ['Topic Introduction', 'Summary of Key Points', 'Discussions', 'Q&A Session', 'Farewell Messages'], 
    'podcast_name': 'XTrends', 
    'podcast_tagline': 'Your very own podcast catered to the current trending topics ', 
    'output_language': 'English', 
    'user_instructions': 'Make if fun and engaging', 
    'engagement_techniques': ['Rhetorical Questions', 'Personal Testimonials', 'Quotes', 'Anecdotes', 'Analogies', 'Humor'], 
    'creativity': 0.75
}


app = Flask(__name__)
CORS(app)
load_dotenv()


trending_news_config = {
    'conversation_style': ['Engaging', 'Fast-paced'],
    'roles_person1': 'main summarizer',
    'roles_person2': 'subject matter expert',
    'dialogue_structure': ['Intro', 'Discussion', 'Outro'],
    'podcast_name': 'Rela Ai',
    'podcast_tagline': 'Trendy podcasts on demand',
    'output_language': 'English',
    'user_instructions': 'Make it fun',
    'engagement_techniques': ['Quotes', 'Humor'],
    'creativity': 0.75
}


@app.route('/', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({"status": "Flask app is runningggg!"})




@app.route('/generate-podcast', methods=['POST'])
def generate():
    print("in function -----------------")
    requestObject = request.get_json()
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



    

@app.route('/audio/<filename>', methods=['GET'])
def serve_audio(filename):
    return send_from_directory("data/audio", filename)

if __name__ == '__main__':
    app.run(port=8000, debug=True)


