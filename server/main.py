from collections import defaultdict

from fastapi import FastAPI
from pydantic import BaseModel

from natasha import (
    Segmenter,
    NewsEmbedding,
    NewsNERTagger,
    Doc
)

app = FastAPI()


segmenter = Segmenter()
emb = NewsEmbedding()
ner_tagger = NewsNERTagger(emb)


class TextRequest(BaseModel):
    text: str


@app.post("/extract")
def extract_places(request: TextRequest):
    text = request.text

    doc = Doc(text)
    doc.segment(segmenter)
    doc.tag_ner(ner_tagger)

    word_types = defaultdict(lambda: defaultdict(int))
    
    for span in doc.spans:
        word = span.text
        word_type = span.type
        word_types[word][word_type] += 1
        print(f"DEBUG: {word} -> {word_type}")

    places = []

    for word, types in word_types.items():
        total = sum(types.values())
        loc_count = types.get("LOC", 0)
        if loc_count > 0 and (loc_count / total) >= 0.5:
            print(f"ДОБАВЛЕНО: {word} (LOC={loc_count}/{total})")
            places.append(word)

    places = list(set(places))

    return {"places": places}