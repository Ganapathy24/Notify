# Generated by Django 3.0.3 on 2020-06-23 09:54

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('kathi', '0008_auto_20200623_1459'),
    ]

    operations = [
        migrations.AlterField(
            model_name='activity',
            name='submission_remark',
            field=models.CharField(max_length=500),
        ),
        migrations.AlterField(
            model_name='activity',
            name='work_remark',
            field=models.CharField(max_length=500),
        ),
    ]
