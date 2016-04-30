feedify.factory('FeedModel', ['$resource',
    function($resource) {

        return $resource(config.apiUrl, {}, {
            getFeeds: {
                method: "GET",
                url: config.apiUrl+"/feeds"
            },
            addFeed: {
                method: "POST",
                url: config.apiUrl+"/feed"
            },
            getArticles: {
              method: "GET",
              url: config.apiUrl+"/feeds/articles/:page"
            },
            getFeedInfos: {
              method: "GET",
              url: config.apiUrl+"/feed/:id"
            },
            getFeedArticles: {
              method: "GET",
              url: config.apiUrl+"/feed/:id/articles/:page"
            },
            markArticleAsRead: {
              method: "POST",
              url: config.apiUrl+"/article/as_read/:id"
            },
            markFeedAsRead: {
              method: "POST",
              url: config.apiUrl+"/feed/as_read/:id"
            },
            updateFeedInfos: {
              method: "PUT",
              url: config.apiUrl+"/feed/:id"
            },
            deleteFeed: {
              method: "DELETE",
              url: config.apiUrl+"/feed/:id"
            },
            updateFeedWorker: {
              method: "GET",
              url: config.apiUrl+"/worker/refresh/feed/:id"
            }
        });
    }
]);
