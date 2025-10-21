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
    'conversation_style': ['Dynamic', 'Engaging', 'Authoritative', 'Fast-paced'],
    'roles_person1': 'charismatic lead host with sharp news instincts who delivers breaking stories with energy and insight',
    'roles_person2': 'witty, street-smart co-host who brings fresh perspectives, asks the questions everyone is thinking, and adds perfectly-timed humor',
    'podcast_name': 'Rela A-I',
    'podcast_tagline': 'Your pulse on what matters - AI-powered news that hits different',
    'output_language': 'English',
    'user_instructions': '''Do not spell out podcast name, read it normally.
    
    Create an addictive, must-listen news experience that feels like the smartest conversation in the room:
    
    PACING & ENERGY:
    - Start with high energy and maintain momentum throughout
    - Use rapid-fire exchanges and natural interruptions between hosts
    - Build tension and excitement around key developments
    - Vary rhythm - slow down for impact moments, speed up for breaking updates
    
    CONTENT MASTERY:
    - Connect every story to the bigger picture and long-term implications
    - Explain complex topics through pop culture references and relatable analogies
    - Always answer "why should I care?" for each story
    - Reveal hidden connections between seemingly unrelated news items
    - Include surprising facts and "did you know" moments that make people go "whoa"
    
    CONVERSATIONAL EXCELLENCE:
    - Hosts should finish each other's thoughts and build on ideas naturally
    - Include genuine reactions - gasps, laughs, "no way!" moments
    - Use current slang and expressions authentically (but not excessively)
    - Reference viral memes, trending topics, and cultural moments when relevant
    - Create inside jokes and callbacks within the episode
    
    AUDIENCE ENGAGEMENT:
    - Speak directly to listeners as if they're part of the conversation
    - Predict and address listener questions before they ask them
    - Use inclusive language that makes everyone feel welcome
    - Include diverse perspectives and global viewpoints
    - End segments with cliffhangers that keep people listening
    
    CREDIBILITY & TRUST:
    - Always cite sources and explain why they matter
    - Distinguish between facts, opinions, and speculation clearly
    - Call out misinformation when encountered
    - Acknowledge when information is developing or incomplete
    - Show intellectual humility while maintaining confidence''',
    'engagement_techniques': [
        'Breaking news urgency', 
        'Viral moment analysis', 
        'Future predictions', 
        'Hidden connections', 
        'Pop culture parallels',
        'Interactive speculation',
        'Trend forecasting',
        'Reality checks',
        'Myth busting'
    ],
    'creativity': 0.9,
}

pdf_files_config = {
    'conversation_style': ['Intelligent', 'Adaptive', 'Accessible', 'Engaging'],
    'roles_person1': 'expert content interpreter who quickly identifies document types and adapts discussion approach, skilled at making complex material accessible without oversimplifying',
    'roles_person2': 'strategic questioner who connects content to real-world applications, challenges assumptions, and ensures listeners understand practical value and broader implications',
    'podcast_name': 'Rela A-I',
    'podcast_tagline': 'Where documents become discoveries',
    'output_language': 'English',
    'user_instructions': '''Do not spell out podcast name, read it normally.
    
    Transform any PDF content into compelling, accessible conversations that deliver maximum value:
    
    DOCUMENT INTELLIGENCE:
    - Immediately identify the document type (research paper, business report, manual, legal document, book chapter, whitepaper, etc.)
    - Adapt your approach based on document characteristics:
      * Academic papers: Focus on methodology, findings, limitations, and peer review context
      * Business documents: Emphasize strategy, market implications, and competitive insights  
      * Technical manuals: Extract key procedures, best practices, and practical applications
      * Legal documents: Explain implications, rights, obligations, and real-world impact
      * Books/literature: Discuss themes, arguments, narrative structure, and cultural significance
      * Reports/studies: Highlight key data, trends, recommendations, and action items
    
    CONTENT MASTERY:
    - Always reference specific sections, quotes, and data points from the document
    - Explain technical jargon and specialized terminology in plain language
    - Identify the author's main arguments, evidence quality, and potential biases
    - Distinguish between facts, interpretations, recommendations, and opinions
    - Highlight what's innovative, surprising, or contradicts conventional wisdom
    
    CONTEXTUAL INTELLIGENCE:
    - Explain why this document exists and who the intended audience is
    - Place content within relevant industry, academic, or cultural context
    - Connect findings to current events, trends, and broader societal issues
    - Compare with other known works, studies, or perspectives in the field
    - Discuss the document's credibility, limitations, and potential controversies
    
    PRACTICAL VALUE DELIVERY:
    - Always answer "What can listeners do with this information?"
    - Extract actionable insights, tools, and frameworks listeners can apply
    - Identify key takeaways that are memorable and shareable
    - Explain how this content might influence decisions, beliefs, or behaviors
    - Provide guidance on further reading or next steps for interested listeners
    
    CONVERSATION EXCELLENCE:
    - Maintain intellectual rigor while keeping discussions conversational
    - Use analogies, metaphors, and real-world examples to clarify complex concepts
    - Build natural dialogue with questions, reactions, and collaborative insights
    - Show genuine curiosity and model critical thinking for listeners
    - Balance depth with accessibility - go deep without losing general audiences
    
    QUALITY ASSURANCE:
    - Never make claims not supported by the document content
    - Acknowledge when information is incomplete, outdated, or requires verification
    - Clearly separate document content from host commentary and speculation
    - Maintain respect for all document types while providing honest assessment
    - End with clear summary of key points and practical implications''',
    'engagement_techniques': [
        'Document type identification',
        'Content synthesis and analysis', 
        'Technical translation', 
        'Real-world application', 
        'Critical evaluation',
        'Context building',
        'Evidence assessment',
        'Practical extraction',
        'Comparative analysis',
        'Actionable insights delivery'
    ],
    'creativity': 0.7,
}

input_config = {
    'conversation_style': ['Exploratory', 'Curious', 'Adaptive', 'Insightful'],
    'roles_person1': 'versatile host who can dive deep into any topic, skilled at asking probing questions and uncovering fascinating angles',
    'roles_person2': 'analytical co-host who excels at connecting ideas, challenging assumptions, and bringing fresh perspectives to any subject',
    'podcast_name': 'Rela A-I',
    'podcast_tagline': 'Turn your thoughts into conversations',
    'output_language': 'English',
    'user_instructions': '''Do not spell out podcast name, read it normally.
    
    Transform user ideas into captivating discussions that exceed expectations:
    
    TOPIC EXPLORATION:
    - Take the user's input as a starting point, not the endpoint
    - Explore multiple dimensions and angles of the topic
    - Ask "what if" questions and explore hypothetical scenarios
    - Connect the topic to current events, historical context, and future implications
    - Uncover hidden aspects and unconventional perspectives
    
    ADAPTIVE APPROACH:
    - Match your energy and depth to the nature of the topic
    - For serious topics: Be respectful yet engaging, focus on nuance and complexity
    - For creative topics: Be imaginative and playful, encourage wild ideas
    - For personal topics: Be empathetic and relatable, share universal experiences
    - For technical topics: Break down complexity, use analogies, make it accessible
    
    CONVERSATION DYNAMICS:
    - Build naturally on the user's ideas without being repetitive
    - Introduce surprising facts, statistics, or research when relevant
    - Share contrasting viewpoints to create healthy debate
    - Use the "yes, and..." approach to expand on concepts
    - Circle back to connect new points with earlier discussions
    
    AUDIENCE VALUE:
    - Always explain why this topic matters to everyday people
    - Provide practical takeaways and actionable insights
    - Share tools, resources, or next steps for interested listeners
    - Make complex ideas memorable through stories and examples
    - End with thought-provoking questions for further reflection
    
    AUTHENTICITY:
    - Show genuine curiosity about the topic, even if it's unfamiliar
    - Admit when you're learning something new alongside the audience
    - Share relatable reactions and personal connections to the subject
    - Maintain conversational flow with natural pauses and transitions''',
    'engagement_techniques': [
        'Deep dive analysis', 
        'Multi-angle exploration', 
        'Personal connection building', 
        'Practical applications', 
        'Thought experiments',
        'Contrarian perspectives',
        'Real-world examples',
        'Future implications',
        'Actionable insights'
    ],
    'creativity': 0.85,
}


url_config = {
    'conversation_style': ['Investigative', 'Critical', 'Informative', 'Conversational'],
    'roles_person1': 'digital content curator who excels at extracting key insights and placing web content in broader context',
    'roles_person2': 'fact-checking co-host who asks critical questions, verifies claims, and ensures balanced perspectives',
    'podcast_name': 'Rela A-I',
    'podcast_tagline': 'Decoding the digital world, one link at a time',
    'output_language': 'English',
    'user_instructions': '''Do not spell out podcast name, read it normally.
    
    Transform web content into meaningful, critical discussions:
    
    CONTENT ANALYSIS:
    - Summarize the main points clearly without simply reading the content
    - Identify the author's credentials, potential biases, and motivations
    - Distinguish between facts, opinions, speculation, and marketing claims
    - Highlight what's new, surprising, or contradictory to conventional wisdom
    - Place the content within relevant industry, cultural, or historical context
    
    SOURCE EVALUATION:
    - Always mention the source, publication date, and credibility indicators
    - Note if this is breaking news, an opinion piece, research study, or commercial content
    - Call out when information needs verification or when sources are missing
    - Compare with other known sources or conflicting viewpoints when relevant
    - Explain why this particular source and timing matter
    
    CRITICAL THINKING:
    - Ask probing questions about the content's claims and conclusions
    - Explore what's not being said or potential gaps in the information
    - Discuss possible alternative interpretations or explanations
    - Consider who benefits from this narrative and who might disagree
    - Examine the broader implications and unintended consequences
    
    AUDIENCE CONNECTION:
    - Translate industry jargon and technical terms into plain language
    - Explain why this content should matter to different types of people
    - Connect web trends to real-world impact on daily life
    - Provide context for listeners who might not be familiar with the topic
    - Offer practical ways listeners can use this information
    
    DIGITAL LITERACY:
    - Help listeners become better consumers of online information
    - Point out red flags for misinformation or clickbait
    - Explain how algorithms, SEO, or platform incentives might affect content
    - Discuss how to find additional reliable sources on the topic
    - Model healthy skepticism without being cynical''',
    'engagement_techniques': [
        'Source verification', 
        'Bias detection', 
        'Context building', 
        'Fact vs opinion separation', 
        'Alternative perspectives',
        'Digital literacy education',
        'Real-world implications',
        'Critical questioning',
        'Trend analysis'
    ],
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
        conversation_config=pdf_files_config,
        api_key_label=os.getenv("OPENAI_API_KEY")
    )

    return send_file(audio_file)





@app.route("/api/v1/podcast/create/input", methods=["POST"])
def generate_podcast_endpoint():
    data = request.get_json()

    content_form = data.get("contentForm")
    url = data.get("url")
    raw_text = data.get("raw_text")
    OPENAI_KEY = os.getenv("OPENAI_API_KEY")

    longform = content_form == "LONG"

    if url:
        audio_path = generate_podcast(
            urls=[url],
            tts_model="openai",
            longform=longform,
            conversation_config=url_config,
            api_key_label=OPENAI_KEY
        )
    elif raw_text:
        audio_path = generate_podcast(
            text=raw_text,
            tts_model="openai",
            longform=longform,
            conversation_config=input_config,
            api_key_label=OPENAI_KEY
        )
    else:
        return jsonify({"error": "You must provide either 'url' or 'raw_text'"}), 400

    return send_file(audio_path, mimetype="audio/mpeg")









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


