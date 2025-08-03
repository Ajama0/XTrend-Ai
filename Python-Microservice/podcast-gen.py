from dotenv import load_dotenv
from podcastfy.client import generate_podcast
from IPython.display import Audio, display
import os
from flask import Flask, request, jsonify
from flask_cors import CORS
from flask import send_from_directory
from flask import send_file



app = Flask(__name__)
app.config['MAX_CONTENT_LENGTH'] = 50 * 1024 * 1024  # 50MB
CORS(app)
load_dotenv()
#Also Introduce the podcast as Rela A I, pronouncing each letter in A-I clearly

trending_news_config = {
    'conversation_style': ['Engaging', 'Fast-paced'],
    'roles_person1': 'enthusiastic host who makes the it fun and interesting',
    'roles_person2': 'fun, sarcastic co-host who reacts with jokes and surprise',
    'podcast_name': 'Rela A-I',
    'podcast_tagline': 'Your daily dose of podcasts - made from what matters to you ',
    'output_language': 'English',
    'user_instructions': 'do not spell out podcast name, read it normally. Speakers should often overlap in their sentences. Make sure to include a lot of humour.',
    'engagement_techniques': ['Quotes', 'Humour', 'anecdotes', 'Rhetorical questions', 'debating'],
    'creativity': 1,
}

files_config = {
    'conversation_style': ['Thoughtful', 'Educational', 'Accessible'],
    'roles_person1': 'knowledgeable host who adapts their approach based on content, breaking down complex topics with clarity',
    'roles_person2': 'inquisitive co-host who asks clarifying questions and connects content to broader implications',
    'podcast_name': 'Rela A-I',
    'podcast_tagline': 'Deep dives into the documents that shape our world',
    'output_language': 'English',
    'user_instructions': '''Do not spell out podcast name, read it normally. 
    Adapt your discussion style based on the document type you encounter:
    - For research papers: Focus on methodology, key findings, implications, and broader significance
    - For books/stories: Emphasize themes, character development, narrative structure, and cultural context
    - For reports/articles: Highlight main arguments, evidence, and practical applications
    - For any document: Stay faithful to the source material while making it accessible to general audiences
    
    Always use specific quotes and references from the document to support your points.
    Use analogies and real-world examples to explain complex concepts.
    Maintain intellectual rigor while keeping the conversation engaging and conversational.
    If you encounter technical jargon, explain it in simpler terms.
    Connect the document's content to current events or broader societal issues when relevant.''',
    'engagement_techniques': ['Document analysis', 'Key insights extraction', 'Real-world connections', 'Critical thinking', 'Contextual explanations'],
    'creativity': 0.75,
}



@app.route('/api/v1/podcast/create/news', methods=['POST'])
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



@app.route('/api/v1/podcast/create/file', methods=['POST'])
def generate_from_input():
    print("in function----------")

    file = request.files.get('file')
    filename = file.filename
    os.makedirs("data/files", exist_ok=True)
    file_path = os.path.join("data/files", filename)
    file.save(file_path)

    absolute_file_path = os.path.abspath(file_path)
    contentForm = request.form.get("contentForm")
    if not contentForm:
        raise ValueError("Content form not provided in the request")
    
    longform = contentForm == "LONG"


    audio_file = generate_podcast(
        urls=[absolute_file_path],
        tts_model="openai",
        longform=longform,
        conversation_config=files_config,
        api_key_label=os.getenv("OPENAI_API_KEY")
    )

    return send_file(audio_file)





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


