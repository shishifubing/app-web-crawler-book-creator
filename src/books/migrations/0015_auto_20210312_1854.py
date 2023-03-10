# Generated by Django 3.1.7 on 2021-03-12 15:54

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('books', '0014_auto_20210312_1519'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='book',
            name='chapters',
        ),
        migrations.AlterField(
            model_name='book',
            name='author',
            field=models.CharField(default='no author', max_length=200, verbose_name='author'),
        ),
        migrations.AlterField(
            model_name='book',
            name='siteUrl',
            field=models.CharField(default='/no-url/', max_length=200, verbose_name='Site url'),
        ),
        migrations.AlterField(
            model_name='book',
            name='sourceUrl',
            field=models.CharField(default='no source link', max_length=200, verbose_name='Source'),
        ),
        migrations.AlterField(
            model_name='book',
            name='title',
            field=models.CharField(default='no title', max_length=200, verbose_name='title'),
        ),
        migrations.CreateModel(
            name='Chapters',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('book', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='books.book')),
                ('chapters', models.ManyToManyField(to='books.Chapter')),
            ],
        ),
    ]
