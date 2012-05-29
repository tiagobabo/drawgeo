class HomeController < ApplicationController
  def index
  end

  def map
  	@s = Geocoder.search(params[:search]);
  end
end
