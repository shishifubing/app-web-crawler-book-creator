
import requests
from bs4 import BeautifulSoup


url = 'https://www.wuxiaworld.com/novel/i-shall-seal-the-heavens/issth-book-1-chapter-1'
userAgent = {'User-Agent': 'Mozilla/5.0'}
response = requests.get(url, headers=userAgent)
print(response.status_code)
chapter = BeautifulSoup(response.content, 'lxml').find(id='chapter-content')
for script in chapter.findAll('script'):
    script.decompose()
chapter.smooth()
chapterText = chapter.prettify(formatter="html")
with open("./chapter1.html", 'w', encoding="utf-8") as openedFile:
    openedFile.write(chapterText.replace(
        'href="/', 'href=https://www.wuxiaworld.com/'))
