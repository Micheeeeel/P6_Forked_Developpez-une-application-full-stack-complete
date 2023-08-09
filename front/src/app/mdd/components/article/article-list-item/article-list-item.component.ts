import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Article } from 'src/app/core/models/article';

@Component({
  selector: 'app-article-list-item',
  templateUrl: './article-list-item.component.html',
  styleUrls: ['./article-list-item.component.scss'],
})
export class ArticleListItemComponent implements OnInit {
  @Input() article!: Article;
  @Output() articleCommented = new EventEmitter<{
    articleId: number;
    comment: string;
  }>();

  constructor() {}

  ngOnInit(): void {}

  onNewComment(comment: string) {
    this.articleCommented.emit({ articleId: this.article.id, comment });
  }
}