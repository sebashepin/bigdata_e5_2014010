from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from pymongo import Connection
from tweepy import Stream
import json

# Datos API de Twitter
consumer_key="
consumer_secret="
access_token="
access_token_secret="

# Conexion a Mongo
conn = Connection() 		
db = conn.grupo10_taller4
coll = db.colaTuits		

class StdOutListener(StreamListener):
    def on_data(self, data):
        print data
        coll.insert(json.loads(data))
        return True

    def on_error(self, status):
        print status

if __name__ == '__main__':
    l = StdOutListener()
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    stream = Stream(auth, l)
    stream.filter(follow=['14834302', '133945128', '33884545'])
