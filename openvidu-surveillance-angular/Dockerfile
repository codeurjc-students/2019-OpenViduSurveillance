FROM node:12.2.0 as build
WORKDIR /app

COPY package.json /app/package.json
RUN npm install
RUN npm install -g @angular/cli@7.3.9

COPY . /app

RUN ng build --output-path=dist
# base image
FROM nginx:1.16.0-alpine

# copy artifact build from the 'build environment'
COPY --from=build /app/dist /usr/share/nginx/html

# expose port 80
EXPOSE 80

# run nginx
CMD ["nginx", "-g", "daemon off;"]


