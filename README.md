# MicroK8s Playground

## Experiment with Production-Ready Fullstack Applications

This playground is designed for experimenting with various technologies that are geared towards production-ready fullstack applications. At present, it features a frontend and backend with Redis integration. However, this is just the beginning - there's definitely more to come!

Feel free to explore the site at: [https://130.61.237.219.nip.io](https://130.61.237.219.nip.io). Your feedback and suggestions are always welcome!

## Kubernetes Setup

Follow these steps to set up Kubernetes:

1. **Install MicroK8s**

   Follow the [Getting Started Guide](https://microk8s.io/docs/getting-started) to install MicroK8s on your system.

2. **Enable Addons**

   Execute the following commands to enable necessary addons:

    ```bash
    microk8s enable dns
    microk8s enable ingress
    microk8s enable cert-manager
    IP=$(curl -s ipinfo.io/ip)
    microk8s enable metallb:$IP-$IP
    ```

3. **Choose an Overlay Network**

   As of now, 'prod' is the most stable overlay network to use with the provided overlays.

4. **Access Your Services**

   You can access your services by entering the IP address in the browser. If you plan to host multiple services on a single IP, consider using a service like [nip.io](https://nip.io) to redirect your requests to the IP with the URL as the hostname.

## Oracle VM Configuration

The cluster is currently hosted on Oracle's generous free tier. To get started, simply spin up a VM there. If you encounter any issues or need assistance, feel free to open an issue.

Please note that Oracle pre-configures certain settings, and getting MicroK8s to work out-of-the-box may require some additional steps. I've found the following guides useful in this regard. However, I must emphasize that I am not a security expert. Therefore, please be aware that this setup may not represent the most secure configuration.

For configuring iptables, you can refer to this [guide](https://github.com/canonical/microk8s/issues/854#issuecomment-1716576495).
