import requests
import re
import os
from bs4 import BeautifulSoup, SoupStrainer


def getSitemap():
    sitemapURL = 'https://www.wuxiaworld.com/sitemap/chapters/1'
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
               'Accept': 'text/html'}
    response = requests.get(sitemapURL, headers=headers)
    if (response.status_code == 200):
        cssSelectorChapterText = '#chapter-content'
        cssSelectorRemoveElements = 'script, style, .chapter-nav'
        try:
            chapter = BeautifulSoup(response.content, 'lxml-xml')
        except IndexError:
            print('no results')
        else:
            with open("./sitemaps/sitemap1.xml", 'w', encoding="utf-8") as openedFile:
                openedFile.write(chapter.prettify(formatter="html"))


def getLinksFromSitemaps():
    with open("./sitemaps/sitemap.xml", 'r', encoding="utf-8") as openedSitemap1, open("./sitemaps/sitemap2.xml", 'r', encoding="utf-8") as openedSitemap2:
        onlyLinks = SoupStrainer('loc')
        sitemmap1 = BeautifulSoup(
            openedSitemap1, 'lxml-xml', parse_only=onlyLinks)
        sitemmap2 = BeautifulSoup(
            openedSitemap2, 'lxml-xml', parse_only=onlyLinks)

        links = []
        for link in sitemmap1.findAll(string=re.compile(
                'https://www.wuxiaworld.com/novel/7-killers')):
            links.append(link.string.strip())
        for link in sitemmap2.findAll(string=re.compile(
                'https://www.wuxiaworld.com/novel/7-killers')):
            links.append(link.string.strip())

        return links


def getChapter(domainURL, chapterURL):
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
               'Accept': 'text/html'}
    response = requests.get(chapterURL, headers=headers)
    if (response.status_code == 200):
        cssSelectorChapterText = '#chapter-content'
        cssSelectorRemoveElements = 'script, style, .chapter-nav'
        try:
            chapter = BeautifulSoup(response.content, 'html.parser')
            chapterText = chapter.select(cssSelectorChapterText, limit=1)[0]
        except IndexError:
            print('no results')
        else:
            chapterText['id'] = 'chapter-1'
            chapterText['class'] = 'chapter-wrapper'
            for uselessElement in chapterText.select(cssSelectorRemoveElements):
                uselessElement.decompose()
            for relativeLink in chapterText(href=re.compile('^/')):
                relativeLink['href'] = domainURL + relativeLink['href']
            for relativeLink in chapterText(src=re.compile('^/')):
                relativeLink['src'] = domainURL+relativeLink['src']
            chapter.smooth()
            global chapterCount
            chapterCountString = str(chapterCount)
            placeholder = '000000'
            chapterCountString = placeholder[0:(
                6-len(chapterCountString)-1)]+chapterCountString

            with open(f'./book/chapters/chapter_{chapterCountString}.html', 'w', encoding="utf-8") as openedFile:
                openedFile.write(chapterText.prettify(formatter="html"))
                chapterCount += 1
    else:
        print(chapterURL, response.status_code)


domainURL = 'https://www.wuxiaworld.com'
chapterCount = 1
while True:
    try:
        os.makedirs('./book/chapters')
    except FileExistsError:
        break
    else:
        break

for link in getLinksFromSitemaps():
    getChapter(domainURL, link)
