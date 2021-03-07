from bs4 import BeautifulSoup, SoupStrainer
import re
import requests

sitemapURL = 'https://www.wuxiaworld.com/sitemap/chapters/2'
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
           'Accept': 'text/html'}
response = requests.get(sitemapURL, headers=headers)

if (response.status_code == 200):
    cssSelectorChapterText = '#chapter-content'
    cssSelectorRemoveElements = 'script, style, .chapter-nav'
    try:
        chapter = BeautifulSoup(response.content, 'lxml-xml')
        #chapterText = chapter.select(cssSelectorChapterText, limit=1)[0]
    except IndexError:
        print('no results')
    else:
        with open("./sitemap2.xml", 'w', encoding="utf-8") as openedFile:
            openedFile.write(chapter.prettify(formatter="html"))
