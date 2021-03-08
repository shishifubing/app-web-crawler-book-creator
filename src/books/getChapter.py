
from books.models import Book, Chapter


def getSitemap():
    sitemapURL = 'https://www.wuxiaworld.com/sitemap/chapters/2'
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
               'Accept': 'text/html'}
    response = requests.get(sitemapURL, headers=headers)
    if (response.status_code == 200):
        try:
            onlyLinks = SoupStrainer('loc')
            sitemap = BeautifulSoup(
                response.content, 'lxml-xml', parse_only=onlyLinks)
        except IndexError:
            print('no results')
        else:
            with open('./books/logic/sitemaps/chapters2.xml', 'w', encoding="utf-8") as openedFile:
                openedFile.write(sitemap.prettify(formatter="html"))


def getChapter(self, domainURL, chapterURL):
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
               'Accept': 'text/html'}
    response = requests.get(chapterURL, headers=headers)
    if (response.status_code == 200):
        cssSelectorChapterText = '#chapter-content'
        cssSelectorRemoveElements = 'script, style, .chapter-nav'
        try:
            chapter = BeautifulSoup(response.content, 'html.parser')
            chapterText = chapter.select(
                cssSelectorChapterText, limit=1)[0]
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
