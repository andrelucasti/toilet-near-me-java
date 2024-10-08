import {check} from 'k6';
import http from 'k6/http';

export const options = {
    stages: [
        { target: 0, duration: '10s' },
        { target: 5, duration: '60s' },
        { target: 10, duration: '60s' },
        { target: 50, duration: '180s' },
    ],
};

export default function() {
    const params = {
        headers: {
            'Content-Type': 'application/json'
        },
    };

    const payload = JSON.stringify({
        description: 'This is a kube test toilet - test kube',
        latitude: 4.0,
        longitude: 1.0,
        price: 1,
        customerId: 'f1eb89be-5d99-4ad5-822e-89793be98e58'
    })

    let response = http.post("http://kong-kong-proxy.kong/toilet", payload, params);
    check(response, {
        'status is 201': (r) => r.status === 201
    });
}