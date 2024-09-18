function removeItem(url, element) {
    if (!url) { // in case the element is newly added
        element.remove();
        return;
    }

    fetch(url, {
        method: 'DELETE',
    }).then((response) => {
        if (!response.ok) {
            alert(response.statusText);
            console.error('Error removing item');
        } else {
            element.remove();
            window.location.reload();
        }
    }).catch((error) => {
        console.error(error);
    });
}