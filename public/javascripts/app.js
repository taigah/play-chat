async function all() {
    return fetch("/messages").then(res => res.json())
}

const csrfToken = JSON.parse(atob(document.cookie.split("; ").filter(c => c.split("=")[0] === "PLAY_SESSION").shift().split("=")[1].split('.')[1])).data.csrfToken

const { useState, useEffect, useCallback, useRef } = React

function Chat() {
    const [messages, setMessages] = useState([])
    const inputRef = useRef()

    const refresh = useCallback(() => {
        return all().then(setMessages)
    })

    useEffect(() => {
        refresh()
    }, [])

    const onSubmit = useCallback(async (e) => {
        e.preventDefault()

        const res = await fetch("/messages", {
            method: "POST",
            body: new URLSearchParams(new FormData(e.target))
        })

        if (res.status >= 400 && res.status < 600) {
            const body = await res.json()
            alert(JSON.stringify(body, null, "  "))
            return
        }

        inputRef.current.value = ""

        await refresh()
    })

    return <div>
        <ul>
            {messages.map((message, i) => <li key={ i }>
                { message.content }
            </li>)}
        </ul>
        <form onSubmit={ onSubmit }>
            <input type="text" ref={ inputRef } placeholder="Message" name="content" required maxLength="100" />
            <input type="hidden" value={ csrfToken } name="csrfToken" />
            <button type="submit">Envoyer</button>
        </form>
    </div>
}

ReactDOM.render(<Chat></Chat>, document.getElementById("chat"))
