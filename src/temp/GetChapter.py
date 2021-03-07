import requests
import re
from bs4 import BeautifulSoup, SoupStrainer

siteURL = 'https://www.wuxiaworld.com'
chapterURL = siteURL+'/novel/i-shall-seal-the-heavens/issth-book-1-chapter-1'
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
           'Accept': 'text/html'}
response = requests.get(chapterURL, headers=headers)
print(response.status_code)

chapter = BeautifulSoup(response.content, 'html.parser')
cssSelector = '#chapter-content'
try:
    chapterText = chapter.select(cssSelector, limit=1)[0]
except IndexError:
    print('no results')
else:
    chapterText['id'] = 'chapter-1'
    chapterText['class'] = 'chapter-wrapper'
    for uselessElement in chapterText(['script', 'style']):
        uselessElement.decompose()
    for relativeLink in chapterText(href=re.compile('^/')):
        relativeLink['href'] = siteURL+relativeLink['href']
    chapter.smooth()
    with open("./chapter1.html", 'w', encoding="utf-8") as openedFile:
        openedFile.write(chapterText.prettify(formatter="html"))
