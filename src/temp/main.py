import requests
from bs4 import BeautifulSoup


url = 'https://www.wuxiaworld.com/novel/i-shall-seal-the-heavens/issth-book-1-chapter-1'
userAgent = {'User-Agent': 'Mozilla/5.0'}
response = requests.get(url, headers=userAgent)
# getting chapter
print(response.status_code)

chapter = BeautifulSoup(response.content, 'lxml').find(id='chapter-content')


def recursiveParsing(node, chapterText):
    try:
        if (node.name == 'p'
                or node.name == 'a'):
            line = node.prettify(formatter="html").replace(
                'href="/', 'href="https://www.wuxiaworld.com/')
            chapterText.append(line)
        else:
            for childNode in node.children:
                recursiveParsing(childNode, chapterText)
    except AttributeError:
        pass


with open("./chapter1.html", 'w', encoding="utf-8") as openedFile:
    chapterText = []
    recursiveParsing(chapter, chapterText)
    for node in chapterText:
        openedFile.write(node)
