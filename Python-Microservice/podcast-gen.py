import dotenv
from dotenv import load_dotenv
from podcastfy.client import generate_podcast
from IPython.display import Audio, display
import os


trending_news_config= {
    'conversation_style': ['Engaging', 'Fast-paced', 'Enthusiastic', 'Educational'], 
    'roles_person1': 'main summarizer', 
    'roles_person2': 'Subject matter expert', 
    'dialogue_structure': ['Topic Introduction', 'Summary of Key Points', 'Discussions', 'Q&A Session', 'Farewell Messages'], 
    'podcast_name': 'XTrend Ai', 
    'podcast_tagline': 'Your very own podcast catered to the current trending topics ', 
    'output_language': 'English', 
    'user_instructions': 'Make if fun and engaging', 
    'engagement_techniques': ['Rhetorical Questions', 'Personal Testimonials', 'Quotes', 'Anecdotes', 'Analogies', 'Humor'], 
    'creativity': 0.75
}



def embed_audio(audio_file):
	"""
	Embeds an audio file in the notebook, making it playable.

	Args:
		audio_file (str): Path to the audio file.
	"""
	try:
		display(Audio(audio_file))
		print(f"Audio player embedded for: {audio_file}")
	except Exception as e:
		print(f"Error embedding audio: {str(e)}")
		



"""
use topic as parameter if url isnt good.

"""


if __name__ == "__main__":
    load_dotenv()
    OPENAI_KEY = os.getenv("OPENAI_API_KEY")
	
    audio_file = generate_podcast(
	urls=["https://www.businessinsider.com/tesla-showroom-design-architecture-marketing-perfect-targets-elon-musk-protests-2025-4"],
	tts_model="openai",
	longform=False, 
	conversation_config=trending_news_config,
	api_key_label=OPENAI_KEY
	)   
    # Embed the audio file generated from transcript
    embed_audio(audio_file)
